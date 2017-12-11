import java.util.*;
import java.util.function.BinaryOperator;

import static java.util.stream.Collectors.toMap;

class Solution {

    public int solution(String S) {

        BinaryOperator<String> addCallDuration = new BinaryOperator<String>() {
            @Override
            public String apply(String s, String s2) {
                String split1[] = s.split(":");
                String split2[] = s2.split(":");

                Integer[] dur1 = { Integer.valueOf(split1[0].trim()), Integer.valueOf(split1[1].trim()),
                        Integer.valueOf(split1[2].trim()) };
                Integer[] dur2 = { Integer.valueOf(split2[0].trim()), Integer.valueOf(split2[1].trim()),
                        Integer.valueOf(split2[2].trim()) };

                Integer[] durationRes = addHours(dur1[0], dur2[0],
                        addMinutes(dur1[1], dur2[1], addSeconds(dur1[2], dur2[2])));

                return durationRes[0] + ":" + durationRes[1] + ":" + durationRes[2];
            }

            private Integer[] addHours(Integer h1, Integer h2, Integer[] minSecRes) {

                Integer hRes = h1 + h2;
                if (minSecRes[0] > 59) {
                    hRes++;
                    minSecRes[0] = minSecRes[0] - 60;
                }

                return new Integer[] { hRes, minSecRes[0], minSecRes[1] };
            }

            private Integer addSeconds(Integer sec1, Integer sec2) {
                return sec1 + sec2;
            }

            private Integer[] addMinutes(Integer m1, Integer m2, Integer sec) {
                Integer mRes = m1 + m2;
                Integer sRes = sec;
                if (sec > 59) {
                    mRes++;
                    sRes = sec - 60;
                }

                return new Integer[] { mRes, sRes };
            }
        };

        HashMap<String, String> map = (HashMap<String, String>) Arrays.stream(S.split("\n"))
                .parallel()
                .map(s -> s.split(","))
                .collect(toMap(e -> e[1], e -> e[0], addCallDuration));

        BinaryOperator<String> count =
                new BinaryOperator<String>() {

                    @Override
                    public String apply(String s, String s2) {
                        return String.valueOf(getSumFrom(s) + getSumFrom(s2));
                    }

                    private Integer getSumFrom(String duration) {

                        if (!duration.contains(":")) {
                            return Integer.valueOf(duration);
                        }

                        String[] split1 = duration.split(":");
                        Integer[] dur1 = { Integer.valueOf(split1[0]), Integer.valueOf(split1[1]),
                                Integer.valueOf(split1[2]) };
                        Integer minutesDuration = getTotalMinutesOfCall(dur1);

                        if (minutesDuration < 5) {
                            return 3 * minutesDuration * 60;
                        } else {
                            return 150 * minutesDuration;
                        }
                    }

                    private Integer getTotalMinutesOfCall(Integer[] callTime) {
                        Integer res = callTime[0] * 60 + callTime[1];
                        if (callTime[2] > 0) {
                            res++;
                        }
                        return res;
                    }
                };

        String summary =
                map.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .skip(1)
                        .map(Map.Entry::getValue)
                        .reduce(count).orElse("Error");

        if (summary.contains(":")) {
            summary = count.apply(summary, "0");
        }

        return Integer.valueOf(summary);
    }
}