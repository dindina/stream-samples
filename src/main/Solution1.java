public class Solution1 {

    public int solution1(int[] A) {
        // Implement your solution here
        if (A == null || A.length == 0) {
            return -1;
        }
        // target for each box: 10
        final long targetBricks = 10;
        long totalTargetBricks = A.length * targetBricks;

        long currentTotalBricks = 0;
        for (int brick : A) {
            currentTotalBricks += brick;
        }
        // check if the total sum of bricks matches the total reqiored.
        // if not return -1
        if (currentTotalBricks != totalTargetBricks)
            return -1;

        long totalMoves = 0;
        for (int i = 0; i < A.length - 1; i++) {
            long diff = A[i] - targetBricks;
            if (diff != 0) {

                totalMoves += Math.abs(diff);

                A[i + 1] += diff;
            }
        }

    }
}
