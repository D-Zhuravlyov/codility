public class SolutionTest {

    public static void main(String[] args) {
        Solution s = new Solution();


        String string = "00:01:07,400-234-090\n"
                + "00:05:01,701-080-080\n"
                + "00:05:00,400-234-090";
              //  + "00:01:01,555-000-999\n"
             //   + "00:01:01,544-000-999";

        System.out.println("RESULT: " + s.solution(string));
    }
}
