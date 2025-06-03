package com.dina.serialize;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectInputFilter;

// Your ExternalizableUserProfile class (slightly simplified for focus)
class FilteredUserProfile implements Externalizable {
    private String username;
    private int age;

    public FilteredUserProfile() { // Required public no-arg constructor
        System.out.println("FilteredUserProfile no-arg constructor called");
    }

    public FilteredUserProfile(String username, int age) {
        this.username = username;
        this.age = age;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println("FilteredUserProfile.writeExternal called for " + username);
        out.writeUTF(username);
        out.writeInt(age);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.username = in.readUTF();
        this.age = in.readInt();
        System.out.println("FilteredUserProfile.readExternal called, user: " + username + ", age: " + age);
    }

    @Override
    public String toString() {
        return "FilteredUserProfile{" + "username='" + username + '\'' + ", age=" + age + '}';
    }
}

// A class we might want to block
class MaliciousGadget implements Serializable {
    private static final long serialVersionUID = 1L;
    private String command;

    public MaliciousGadget(String command) {
        this.command = command;
        System.out.println("MaliciousGadget constructor called with command: " + command);
    }

    // Imagine this method could be exploited during or after deserialization
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        System.out.println("MaliciousGadget.readObject() called! Executing: " + command);
        // In a real scenario, this might Runtime.getRuntime().exec(command);
    }

    @Override
    public String toString() {
        return "MaliciousGadget{command='" + command + "'}";
    }
}

public class SerializationFilterExample {

    private static final String USER_FILE = "filtereduser.ser";
    private static final String GADGET_FILE = "gadget.ser";

    public static void main(String[] args) {
        // 1. Create and serialize some objects
        FilteredUserProfile user = new FilteredUserProfile("filterUser", 35);
        MaliciousGadget gadget = new MaliciousGadget("do_evil_stuff");

        serializeObject(user, USER_FILE);
        serializeObject(gadget, GADGET_FILE);

        System.out.println("\n--- Attempting deserialization WITHOUT a restrictive filter (default behavior) ---");
        // Depending on JVM defaults, this might still work or have some basic
        // protection.
        // For this demo, let's assume it would deserialize both if no explicit filter
        // is set.
        deserializeObject(USER_FILE, null, "User (no filter)");
        deserializeObject(GADGET_FILE, null, "Gadget (no filter)");

        System.out.println("\n--- Attempting deserialization WITH a stream-specific filter ---");

        // Filter 1: Allow only FilteredUserProfile from com.dina.serialize, and basic
        // Java types.
        // Deny everything else explicitly with "!*".
        String allowUserPattern = "com.dina.serialize.FilteredUserProfile;" +
                "java.base/*;" + // Allow basic java types often needed
                "!*"; // Deny all others
        ObjectInputFilter userOnlyFilter = ObjectInputFilter.Config.createFilter(allowUserPattern);

        System.out.println("\nUsing filter: " + allowUserPattern);
        deserializeObject(USER_FILE, userOnlyFilter, "User (userOnlyFilter)");
        deserializeObject(GADGET_FILE, userOnlyFilter, "Gadget (userOnlyFilter)");

        System.out.println("\n--- Attempting deserialization WITH a more restrictive filter (e.g., by package) ---");
        // Filter 2: Allow any class in com.dina.serialize and sub-packages, plus
        // java.base, deny others.
        // Also set some resource limits.
        String packageAllowPattern = "maxdepth=5;maxarray=1000;" +
                "com.dina.serialize.**;" +
                "java.base/*;" +
                "!*";
        ObjectInputFilter packageFilter = ObjectInputFilter.Config.createFilter(packageAllowPattern);

        System.out.println("\nUsing filter: " + packageAllowPattern);
        deserializeObject(USER_FILE, packageFilter, "User (packageFilter)");
        // MaliciousGadget is also in com.dina.serialize, so it would be allowed by this
        // package filter
        // unless we explicitly deny it or it's in a different, non-allowed package.
        // For this example, let's assume MaliciousGadget is something we *always* want
        // to block if possible.
        // A better filter would be more specific if MaliciousGadget was in the same
        // package:
        // "com.dina.serialize.FilteredUserProfile;!com.dina.serialize.MaliciousGadget;com.dina.serialize.**;java.base/*;!*"

        deserializeObject(GADGET_FILE, packageFilter, "Gadget (packageFilter)");

        System.out.println("\n--- Attempting deserialization WITH a filter that DENIES MaliciousGadget explicitly ---");
        String denyGadgetPattern = "com.dina.serialize.FilteredUserProfile;" +
                "!com.dina.serialize.MaliciousGadget;" + // Explicitly deny
                "com.dina.serialize.**;" + // Allow others in package
                "java.base/*;" +
                "!*";
        ObjectInputFilter denyGadgetFilter = ObjectInputFilter.Config.createFilter(denyGadgetPattern);
        System.out.println("\nUsing filter: " + denyGadgetPattern);
        deserializeObject(USER_FILE, denyGadgetFilter, "User (denyGadgetFilter)");
        deserializeObject(GADGET_FILE, denyGadgetFilter, "Gadget (denyGadgetFilter)");

    private static void serializeObject(Object obj, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
            System.out.println("Serialized " + obj.getClass().getSimpleName() + " to " + filename);
        } catch (IOException e) {
            System.err.println("Error serializing " + obj.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static void deserializeObject(String filename, ObjectInputFilter filter, String description) {
        System.out.print("Deserializing " + description + " from " + filename + ": ");
        try (FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream ois = new ObjectInputStream(fis)) {

            if (filter != null) {
                ois.setObjectInputFilter(filter);
                System.out.print("[Filter Active] ");
            } else {
                System.out.print("[No Custom Filter] ");
            }

            Object deserializedObj = ois.readObject();
            System.out.println("SUCCESS - " + deserializedObj);
        } catch (java.io.InvalidClassException e) {
            System.out.println("FAILED (FILTERED) - " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("FAILED (Error) - " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
