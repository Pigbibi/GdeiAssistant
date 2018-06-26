package com.gdeiassistant.gdeiassistant.Tools;

public class ScheduleColorUtils {

    /**
     * 通过单元格位置计算赋予课程的成绩背景颜色
     *
     * @param position
     * @return
     */
    public static String getScheduleColor(int position) {
        //根据课表单元块的位置设置颜色
        int column = position % 7;
        int row = position / 7;
        switch (row) {
            //匹配位置对应的行数
            case 0:
            case 1:
                //第1,2行
                switch (column) {
                    case 0:
                        //周一
                        return "#07b2ff";

                    case 1:
                        //周二
                        return "#385ef9";

                    case 2:
                        //周三
                        return "#f91d1b";

                    case 3:
                        //周四
                        return "#fea510";

                    case 4:
                        //周五
                        return "#f8640e";

                    case 5:
                        //周六
                        return "#1ae961";

                    case 6:
                        //周日
                        return "#e844bb";
                }

            case 2:
            case 3:
                switch (column) {
                    case 0:
                        //周一
                        return "#fd7471";

                    case 1:
                        //周二
                        return "#fdca00";

                    case 2:
                        //周三
                        return "#21a5ed";

                    case 3:
                        //周四
                        return "#fea716";

                    case 4:
                        //周五
                        return "#61b822";

                    case 5:
                        //周六
                        return "#375cef";

                    case 6:
                        //周日
                        return "#ff6160";
                }

            case 4:
                switch (column) {
                    case 0:
                        //周一
                        return "#fba516";

                    case 1:
                        //周二
                        return "#fba516";

                    case 2:
                        //周三
                        return "#11f660";

                    case 3:
                        //周四
                        return "#f82320";

                    case 4:
                        //周五
                        return "#f3cd1e";

                    case 5:
                        //周六
                        return "#f9201e";

                    case 6:
                        //周日
                        return "#68d31b";
                }

            case 5:
            case 6:
                switch (column) {
                    case 0:
                        //周一
                        return "#64cf14";

                    case 1:
                        //周二
                        return "#37a6e2";

                    case 2:
                        //周三
                        return "#fb2927";

                    case 3:
                        //周四
                        return "#fcca04";

                    case 4:
                        //周五
                        return "#fc5653";

                    case 5:
                        //周六
                        return "#fd7c22";

                    case 6:
                        //周日
                        return "#37b4ec";
                }

            case 7:
            case 8:
                switch (column) {
                    case 0:
                        //周一
                        return "#fa7d25";

                    case 1:
                        //周二
                        return "#4063f2";

                    case 2:
                        //周三
                        return "#57bb0f";

                    case 3:
                        //周四
                        return "#ed44bf";

                    case 4:
                        //周五
                        return "#16ea5f";

                    case 5:
                        //周六
                        return "#fac800";

                    case 6:
                        //周日
                        return "#f42321";
                }

            case 9:
            case 10:
                switch (column) {
                    case 0:
                        //周一
                        return "#f92c2b";

                    case 1:
                        //周二
                        return "#4669fa";

                    case 2:
                        //周三
                        return "#40c1fb";

                    case 3:
                        //周四
                        return "#fe6a14";

                    case 4:
                        //周五
                        return "#ffab1f";

                    case 5:
                        //周六
                        return "#f8605d";

                    case 6:
                        //周日
                        return "#10e157";
                }

            case 11:
            case 12:
                switch (column) {
                    case 0:
                        //周一
                        return "#f7c500";

                    case 1:
                        //周二
                        return "#f83e3b";

                    case 2:
                        //周三
                        return "#ea52c0";

                    case 3:
                        //周四
                        return "#099fd4";

                    case 4:
                        //周五
                        return "#f55a57";

                    case 5:
                        //周六
                        return "#56bc0d";

                    case 6:
                        //周日
                        return "#f37821";
                }

            default:
                return "#49bef8";
        }
    }
}
