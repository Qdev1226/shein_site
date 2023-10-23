import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakeContents {
    static String contentstemplate;
    static final String titlePattern = "<!-- InstanceBeginEditable name=\"title\" -->.*<title>(.+)</title>.*<!-- InstanceEndEditable -->";
    static final String datePattern = "<!-- InstanceBeginEditable name=\"date\" -->.*((\\d{1,2}\\/\\d{1,2}\\/\\d{2})).*<!-- InstanceEndEditable -->";
    static DateFormat df = new SimpleDateFormat("MM/dd/yy");

    static class Page implements Comparable<Page>{
        String pagename;
        String title;
        Date date;

        public Page(String pagename, String title, Date date) {
            this.pagename = pagename;
            this.title = title;
            this.date = date;
        }

        @Override
        public String toString() {
            return "Page{" +
                    "pagename='" + pagename + '\'' +
                    ", title='" + title + '\'' +
                    ", date='" + date + '\'' +
                    '}';
        }

        @Override
        public int compareTo(Page o) {
            return date.compareTo(o.date);
        }
    }

    public static void main(String[] args) throws IOException, ParseException{
        contentstemplate = readFile("contentstemplate.html", Charset.defaultCharset());
        String table = makeTable();
        String output = contentstemplate.replace("PUTCONTENTSHERE",table);
        FileWriter writer = new FileWriter("contents.html");
        writer.write(output);
        writer.close();
    }

    static String makeTable() throws IOException{
        Path dir = Paths.get("writeups");
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir,"*.html");
        ArrayList<Page> list = new ArrayList<>();
        directoryStream.forEach(p -> {
            try {
                list.add(makePage(p.toString()));
            }
            catch(Exception doesntmatter){
                doesntmatter.printStackTrace();
            }
        });
		//list.forEach(System.out::println);
        Collections.sort(list);
        Collections.reverse(list);
        StringBuilder sb = new StringBuilder();
        list.forEach(p -> sb.append(pageToEntry(p)));
        return sb.toString();
    }

    static String pageToEntry(Page p){
        String s = String.format("<a class=\"pagelinks\" href=\"%s\">%s - %s</a>\n<br/>\n",p.pagename,df.format(p.date),p.title);
        return s;
    }

    static Page makePage(String filename) throws IOException, ParseException {
        String file = readFile(filename,Charset.defaultCharset());
        String dateString = findPattern(file,datePattern);
        String title = findPattern(file,titlePattern);
        Date date = df.parse(dateString);
        return new Page(filename,title,date);
    }
    static String findPattern(String source, String regex){
        Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
        Matcher matcher = pattern.matcher(source);
        matcher.find();
        return matcher.group(1);
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
