import java.io.*;
import com.kmaebashi.henacat.servlet.*;
import com.kmaebashi.henacat.servlet.http.*;

public class GetAddress extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String postalCode = request.getParameter("postalCode");
        String ret;
        if (postalCode.equals("162-0846")) {
            ret = "“Œ‹“sVh‹æs’J¶“à’¬";
        } else if (postalCode.equals("100-0014")) {
            ret = "“Œ‹“sç‘ã“c‹æ‰i“c’¬";
        } else {
            ret = "•s–¾";
        }
        out.print(ret);
    }
}