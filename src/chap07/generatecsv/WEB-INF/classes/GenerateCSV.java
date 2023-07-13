import java.io.*;
import javax.servlet.http.*;

public class GenerateCSV extends HttpServlet {
    private static final String zodiacSigns[] = {
        "‚¨‚Ğ‚Â‚¶À", "‚¨‚¤‚µÀ", "‚Ó‚½‚²À", "‚©‚ÉÀ",
        "‚µ‚µÀ", "‚¨‚Æ‚ßÀ", "‚Ä‚ñ‚Ñ‚ñÀ", "‚³‚»‚èÀ",
        "‚¢‚ÄÀ", "‚â‚¬À", "‚İ‚¸‚ª‚ßÀ","‚¤‚¨À",
    };
    private static final String fortunes[] = {
        "ƒ‰ƒbƒL[", "‚Ó‚Â‚¤", "Åˆ«"
    };
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
         response.setContentType("text/csv;charset=Shift_JIS");
         response.setHeader("Content-Disposition",
                    "attachment; filename=\"horoscope.csv\"");
         PrintWriter out = response.getWriter();

         for (int i = 0; i < zodiacSigns.length; i++) {
             out.print("\"" + zodiacSigns[i] + "\",");
             out.print("\"" + fortunes[(int)(Math.random() * fortunes.length)]
                       + "\"\r\n");
         }

    }
}
