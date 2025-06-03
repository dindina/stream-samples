package com.dina.streams;

import java.lang.Math; // Math is part of java.lang and automatically imported, but explicit for clarity

import java.util.Arrays;

class Solution {
    public int solution(int[] A) {
        if (A == null || A.length == 0) {
            // An empty array implies 0 boxes, 0 target bricks, 0 moves.
            // Or, depending on problem constraints, could be an error.
            // Assuming N >= 1 based on typical contest constraints.
            return 0; // Or -1 if N must be > 0 and 0 is invalid.
                      // Given N boxes (0 to N-1), N >= 1 is implied.
        }

        int N = A.length;
        long targetBricksPerBox = 10; // Target for each box
        long totalTargetBricks = N * targetBricksPerBox;

        // Calculate current total sum of bricks
        long currentTotalBricks = 0;
        for (int bricks : A) {
            currentTotalBricks += bricks;
        }

        A.stream().
        // Check if the total sum of bricks matches the target sum.
        // If not, it's impossible to achieve the goal.
        if (currentTotalBricks != totalTargetBricks) {
            System.out.println("in condition");
            return -1;
        }

        // Initialize total moves
        long totalMoves = 0;

        // Iterate through the boxes from left to right,
        // adjusting the number of bricks in the next box as needed.
        // We only need to iterate up to N-2 because the last box (N-1)
        // will automatically have 10 bricks if the total sum was correct.

        // Calculate the difference from the target for the current box
        // If the current box doesn't have exactly 10 bricks,
        // we must move bricks to/from the next box.

        for (int i = 0; i < N - 1; i++) {
            long difference = A[i] - targetBricksPerBox;

            if (difference != 0) {

                totalMoves += Math.abs(difference);

                A[i + 1] += difference;
            }
            // A[i] is now conceptually "balanced" to 10 for the purpose of further
            // calculations.
        }

        // After the loop, A[N-1] should automatically be 10 if total sum was initially
        // correct.
        // No explicit check needed for A[N-1] as the initial sum check covers it.

        // Ensure totalMoves does not exceed int max value if N and A[i] can be very
        // large.
        // Given typical competitive programming constraints for int arrays, long for
        // sum and moves is safer.
        if (totalMoves > Integer.MAX_VALUE) {
            // This might happen if N is very large and brick counts vary wildly.
            // Problem usually specifies constraints on N and A[K].
            // If moves can exceed int, problem statement usually implies long or special
            // handling.
            // For general contest problems, it's usually within int range.
            return -1; // Or throw an exception, depending on requirements for overflow
        }

        return (int) totalMoves;
    }

    public static void main(String[] args) {
        // int[] nums = new int[] { 11, 10, 8, 12, 8, 10, 11 };
        // int[] nums = new int[] { 7, 15, 10, 8 };
        int[] nums = new int[] { 7, 14, 10 };
        Solution solution = new Solution();
        System.out.println(solution.solution(nums));

    }
}
