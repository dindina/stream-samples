package com.dina.serialize;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class ExternalizableUserProfile implements Externalizable {
    private String username;
    private transient String password; // Will be handled manually
    private int age;

    // Public no-arg constructor is REQUIRED for Externalizable
    public ExternalizableUserProfile() {
        System.out.println("ExternalizableUserProfile no-arg constructor called");
    }

    public ExternalizableUserProfile(String username, String password, int age) {
        this.username = username;
        this.password = password; // Typically, you wouldn't serialize a raw password
        this.age = age;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println("writeExternal called");
        out.writeUTF(username);
        out.writeInt(age);
        // Manually handle 'password' - perhaps encrypt it or simply don't serialize it
        // For this example, let's write a placeholder or an encrypted version
        out.writeUTF(encrypt(password));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        System.out.println("readExternal called");
        this.username = in.readUTF();
        this.age = in.readInt();
        // Manually handle 'password'
        this.password = decrypt(in.readUTF());
    }

    // Dummy encryption/decryption for example purposes
    private String encrypt(String data) {
        if (data == null)
            return "";
        return new StringBuilder(data).reverse().toString() + "_encrypted";
    }

    private String decrypt(String encryptedData) {
        if (encryptedData == null || !encryptedData.endsWith("_encrypted"))
            return "";
        return new StringBuilder(encryptedData.substring(0, encryptedData.length() - "_encrypted".length()))
                .reverse().toString();
    }

    @Override
    public String toString() {
        return "ExternalizableUserProfile{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' + // Be careful logging/printing sensitive data
                ", age=" + age +
                '}';
    }

    public static void main(String[] args) {
        ExternalizableUserProfile user = new ExternalizableUserProfile("john.doe", "secret123", 30);
        String filename = "ExternalizableUserProfile.ser";

        // Serialize
        try (FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
            System.out.println("User serialized: " + user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize
        ExternalizableUserProfile deserializedUser = null;
        try (FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            deserializedUser = (ExternalizableUserProfile) ois.readObject();
            System.out.println("User deserialized: " + deserializedUser);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
