import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Webpage {

    public static void main(String[] args) throws IOException {

        ArrayList<File> udpResultsFiles = new ArrayList<>();
        ArrayList<File> tcpResultsFiles = new ArrayList<>();

        File resultsDir = new File("results");
        File[] allResultsFiles = resultsDir.listFiles();

        for(File f : allResultsFiles){
            if(f.getName().startsWith("UDP")){
                udpResultsFiles.add(f);
            }
            else if(f.getName().startsWith("TCP")){
                tcpResultsFiles.add(f);
            }
        }

        BufferedWriter fout = new BufferedWriter(new FileWriter("results/results.html"));

        fout.write(makeHtml(tcpResultsFiles, udpResultsFiles));
        fout.close();

    }

    public static int getTrials(File file){
        int trials = -1;
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            trials = Integer.parseInt(in.readLine());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return trials;
    }

    public static String getHost(File file){
        String host = "";

        try{
            BufferedReader in = new BufferedReader(new FileReader(file));
            host = in.readLine();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return host;
    }

    public static ArrayList<ArrayList<Double>> getUdpResults(File file){
        ArrayList<ArrayList<Double>> times = new ArrayList<>();

        ArrayList<Double> latency1b = new ArrayList<>();
        ArrayList<Double> latency64b = new ArrayList<>();
        ArrayList<Double> latency1kb = new ArrayList<>();
        ArrayList<Double> tpTimes = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            in.readLine(); //host name

            int trials = Integer.parseInt(in.readLine()); //trials

            latency1b.add(Double.parseDouble(in.readLine()));
            latency64b.add(Double.parseDouble(in.readLine()));
            latency1kb.add(Double.parseDouble(in.readLine()));

            tpTimes.add(Double.parseDouble(in.readLine()));
            tpTimes.add(Double.parseDouble(in.readLine()));
            tpTimes.add(Double.parseDouble(in.readLine()));

            times.add(latency1b);
            times.add(latency64b);
            times.add(latency1kb);
            times.add(tpTimes);



        }
        catch(IOException e){
            e.printStackTrace();
        }

        return times;
    }

    public static ArrayList<ArrayList<Double>> getTcpResults(File file){

        ArrayList<ArrayList<Double>> times = new ArrayList<>();

        ArrayList<Double> latency1b = new ArrayList<>();
        ArrayList<Double> latency64b = new ArrayList<>();
        ArrayList<Double> latency1kb = new ArrayList<>();
        ArrayList<Double> tpTimes = new ArrayList<>();
        ArrayList<Double> mb1Times = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            in.readLine(); //host name

            int trials = Integer.parseInt(in.readLine()); //trials

            latency1b.add(Double.parseDouble(in.readLine()));
            latency64b.add(Double.parseDouble(in.readLine()));
            latency1kb.add(Double.parseDouble(in.readLine()));

            tpTimes.add(Double.parseDouble(in.readLine()));
            tpTimes.add(Double.parseDouble(in.readLine()));
            tpTimes.add(Double.parseDouble(in.readLine()));
            tpTimes.add(Double.parseDouble(in.readLine()));
            tpTimes.add(Double.parseDouble(in.readLine()));

            mb1Times.add(Double.parseDouble(in.readLine()));
            mb1Times.add(Double.parseDouble(in.readLine()));
            mb1Times.add(Double.parseDouble(in.readLine()));

            times.add(latency1b);
            times.add(latency64b);
            times.add(latency1kb);
            times.add(tpTimes);
            times.add(mb1Times);



        }
        catch(IOException e){
            e.printStackTrace();
        }

        return times;
    }

    public static String makeMb1ChartHtml(String host, ArrayList<ArrayList<Double>> udpResults, ArrayList<ArrayList<Double>> tcpResults){

        String udpMb1Results = mb1Times(udpResults);
        String tcpMb1Results = mb1Times(tcpResults);

        String html = "<div class=\"container\">\n" + "\n" +
                "  <canvas id=\"mb1Chart" + host + "\" width=\"800\" height=\"450\"></canvas>\n" +
                "               </div>\n" +
                "<script>\n" +
                "    new Chart(document.getElementById(\"mb1Chart" + host + "\"), {\n" +
                "        type: 'line',\n" +
                "        data: {\n" +
                "            labels: [\"256\", \"512\",\"1024\"], \n" +
                "            datasets: [{\n" +
                "                data:" + udpMb1Results + ",\n" +
                "                label: \"UDP\",\n" +
                "                borderColor: \"#FF0000\",\n" +
                "                fill: false\n" +
                "               },{\n" +
                "                data:" + tcpMb1Results + ",\n" +
                "                label: \"TCP\",\n" +
                "                borderColor: \"#0078ff\",\n" +
                "                fill: false\n" +
                "               }" +
                "           ]\n" +
                "        }," +
                "        options: {\n" +
                "\t\t\t\tresponsive: true,\n" +
                "\t\t\t\ttitle: {\n" +
                "\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\ttext: 'TCP vs. UDP Times to Send 1MB in Varying Message Sizes" + host + "'\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\ttooltips: {\n" +
                "\t\t\t\t\tmode: 'index',\n" +
                "\t\t\t\t\tintersect: false,\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\thover: {\n" +
                "\t\t\t\t\tmode: 'nearest',\n" +
                "\t\t\t\t\tintersect: true\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\tscales: {\n" +
                "\t\t\t\t\txAxes: [{\n" +
                "\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\tscaleLabel: {\n" +
                "\t\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\t\tlabelString: 'Message Size in Bytes'\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}],\n" +
                "\t\t\t\t\tyAxes: [{\n" +
                "\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\tscaleLabel: {\n" +
                "\t\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\t\tlabelString: 'Time in Nanoseconds'\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t});\n" +
                "</script>\n";

        return html;
    }

    public static String makeTpChartHtml(String host, String tbResultsStr){
        //String tbResultsStr = tbResults.toString();
        String html = "<div class=\"container\">\n" + "\n" +
                "  <canvas id=\"tpChart" + host + "\" width=\"800\" height=\"450\"></canvas>\n" +
                "               </div>\n" +
                "<script>\n" +
                "    new Chart(document.getElementById(\"tpChart" + host + "\"), {\n" +
                "        type: 'line',\n" +
                "        data: {\n" +
                "            labels: [\"1K\", \"16K\",\"64K\",\"256K\",\"1M\"], \n" +
                "            datasets: [{\n" +
                "                data:" + tbResultsStr + ",\n" +
                "                label: \"Throughput\",\n" +
                "                borderColor: \"#FF0000\",\n" +
                "                fill: false\n" +
                "               }]\n" +
                "        }," +
                "        options: {\n" +
                "\t\t\t\tresponsive: true,\n" +
                "\t\t\t\ttitle: {\n" +
                "\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\ttext: 'TCP vs. UDP Throughput " + host + "'\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\ttooltips: {\n" +
                "\t\t\t\t\tmode: 'index',\n" +
                "\t\t\t\t\tintersect: false,\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\thover: {\n" +
                "\t\t\t\t\tmode: 'nearest',\n" +
                "\t\t\t\t\tintersect: true\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\tscales: {\n" +
                "\t\t\t\t\txAxes: [{\n" +
                "\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\tscaleLabel: {\n" +
                "\t\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\t\tlabelString: 'Bytes'\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}],\n" +
                "\t\t\t\t\tyAxes: [{\n" +
                "\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\tscaleLabel: {\n" +
                "\t\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\t\tlabelString: 'Mbps'\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t});\n" +
                "</script>\n";

        return html;
    }

    public static String makeLatencyChartHtml(String host, ArrayList<ArrayList<Double>> udpResults, ArrayList<ArrayList<Double>> tcpResults){

        String udpLatencyResults = latencyResultsString(udpResults);
        String tcpLatencyResults = latencyResultsString(tcpResults);

        String html = "<div class=\"container\">\n" + "\n" +
                "  <canvas id=\"latencyChart" + host + "\" width=\"800\" height=\"450\"></canvas>\n" +
                "               </div>\n" +
                "<script>\n" +
                "    new Chart(document.getElementById(\"latencyChart" + host + "\"), {\n" +
                "        type: 'line',\n" +
                "        data: {\n" +
                "            labels: [\"1 byte\", \"64 byte\",\"1 kilobyte\"], \n" +
                "            datasets: [{\n" +
                "                data:" + udpLatencyResults + ",\n" +
                "                label: \"UDP\",\n" +
                "                borderColor: \"#FF0000\",\n" +
                "                fill: false\n" +
                "               },{\n" +
                "                data:" + tcpLatencyResults + ",\n" +
                "                label: \"TCP\",\n" +
                "                borderColor: \"#0078ff\",\n" +
                "                fill: false\n" +
                "               }" +
                "           ]\n" +
                "        }," +
                "        options: {\n" +
                "\t\t\t\tresponsive: true,\n" +
                "\t\t\t\ttitle: {\n" +
                "\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\ttext: 'TCP vs. UDP Latency " + host + "'\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\ttooltips: {\n" +
                "\t\t\t\t\tmode: 'index',\n" +
                "\t\t\t\t\tintersect: false,\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\thover: {\n" +
                "\t\t\t\t\tmode: 'nearest',\n" +
                "\t\t\t\t\tintersect: true\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\tscales: {\n" +
                "\t\t\t\t\txAxes: [{\n" +
                "\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\tscaleLabel: {\n" +
                "\t\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\t\tlabelString: 'Bytes'\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}],\n" +
                "\t\t\t\t\tyAxes: [{\n" +
                "\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\tscaleLabel: {\n" +
                "\t\t\t\t\t\t\tdisplay: true,\n" +
                "\t\t\t\t\t\t\tlabelString: 'Milliseconds'\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t});\n" +
                "</script>\n";

        return html;
    }

    public static String makeHtml(ArrayList<File> tcpResultsFiles, ArrayList<File> udpResultsFiles) {

        StringBuilder sbLatency = new StringBuilder();
        StringBuilder sbTp = new StringBuilder();
        StringBuilder sb1mb = new StringBuilder();

        for(int i = 0; i < tcpResultsFiles.size(); i++){

            String curHost = getHost(tcpResultsFiles.get(i));
            ArrayList<ArrayList<Double>> curTcpResults = getTcpResults(tcpResultsFiles.get(i));
            ArrayList<ArrayList<Double>> curUdpResults = getUdpResults(udpResultsFiles.get(i));

            sbLatency.append(makeLatencyChartHtml(curHost, curUdpResults, curTcpResults));
            sbTp.append(makeTpChartHtml(curHost, tpTimes(curTcpResults)));
            sb1mb.append(makeMb1ChartHtml(curHost, curUdpResults, curTcpResults));

        }


        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.min.js\"></script>\n" +
                "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" +
                "    <title>TCP & UDP Measurements</title>\n" +
                "</head>\n" +
                "<body>\n" +
                sbLatency.toString() +
                sbTp.toString() +
                sb1mb.toString() +
                "</body>";

        return html;
    }

    public static String latencyResultsString(ArrayList<ArrayList<Double>> results) {

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(results.get(0).get(0));
        sb.append(", ");
        sb.append(results.get(1).get(0));
        sb.append(", ");
        sb.append(results.get(2).get(0));
        sb.append("]");

        return sb.toString();
    }

    public static String tpTimes(ArrayList<ArrayList<Double>> results){

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(results.get(3).get(0));
        sb.append(", ");
        sb.append(results.get(3).get(1));
        sb.append(", ");
        sb.append(results.get(3).get(2));
        sb.append(", ");
        sb.append(results.get(3).get(3));
        sb.append(", ");
        sb.append(results.get(3).get(4));
        sb.append("]");

        return sb.toString();
    }

    public static String mb1Times(ArrayList<ArrayList<Double>> results){

        StringBuilder sb = new StringBuilder();

        int i = results.size() - 1;

        sb.append("[");
        sb.append(results.get(i).get(0));
        sb.append(", ");
        sb.append(results.get(i).get(1));
        sb.append(", ");
        sb.append(results.get(i).get(2));
        sb.append("]");

        return sb.toString();

    }
}