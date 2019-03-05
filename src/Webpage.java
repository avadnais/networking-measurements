import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Webpage {

    public static void main(String[] args) throws IOException {
        File udpFile = new File("UDPResults.txt");
        File tcpFile = new File("TCPResults.txt");
        BufferedWriter fout = new BufferedWriter(new FileWriter("results.html"));

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.min.js\"></script>\n" +
                "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" +
                "  <title>TCP & UDP Measurements</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <canvas id=\"myChart\"></canvas>\n" +
                "  </div>\n" +
                "\n" +
                "  <script>\n" + "new Chart(document.getElementById(\"line-chart\"), {\n" +
                "  type: 'line',\n" +
                "  data: {\n" +
                "    labels: [1500,1600,1700,1750,1800,1850,1900,1950,1999,2050],\n" +
                "    datasets: [{ \n" +
                "        data: [86,114,106,106,107,111,133,221,783,2478],\n" +
                "        label: \"Africa\",\n" +
                "        borderColor: \"#3e95cd\",\n" +
                "        fill: false\n" +
                "      }, { \n" +
                "        data: [282,350,411,502,635,809,947,1402,3700,5267],\n" +
                "        label: \"Asia\",\n" +
                "        borderColor: \"#8e5ea2\",\n" +
                "        fill: false\n" +
                "      }, { \n" +
                "        data: [168,170,178,190,203,276,408,547,675,734],\n" +
                "        label: \"Europe\",\n" +
                "        borderColor: \"#3cba9f\",\n" +
                "        fill: false\n" +
                "      }, { \n" +
                "        data: [40,20,10,16,24,38,74,167,508,784],\n" +
                "        label: \"Latin America\",\n" +
                "        borderColor: \"#e8c3b9\",\n" +
                "        fill: false\n" +
                "      }, { \n" +
                "        data: [6,3,2,2,7,26,82,172,312,433],\n" +
                "        label: \"North America\",\n" +
                "        borderColor: \"#c45850\",\n" +
                "        fill: false\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  options: {\n" +
                "    title: {\n" +
                "      display: true,\n" +
                "      text: 'World population per region (in millions)'\n" +
                "    }\n" +
                "  }\n" +
                "});" +
                "        tooltips:{\n" +
                "          enabled:true\n" +
                "        }\n" +
                "      }";

        fout.write(html);
        fout.close();
    }
}
