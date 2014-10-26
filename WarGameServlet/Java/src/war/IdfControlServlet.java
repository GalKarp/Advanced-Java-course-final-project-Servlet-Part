package war;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Missiles.EnemyMissile;
import launchers.EnemyLauncher;
import launchers.IronDome;
import launchers.LauncherDestroyer;

@WebServlet(name = "IdfControlServlet", urlPatterns = { "/IdfControlServlet" })
public class IdfControlServlet extends HttpServlet {

  public static final String LAUNCHER = "launcher";
  public static final String MISSILE = "missile";
  public static final int TAKES_TIME_MIN = 1;
  public static final int TAKES_TIME_MAX = 10;
  private War war;
  private boolean enterOnce = true;
  private static final long serialVersionUID = 1L;
  private String status;
  private Queue<EnemyMissile> missiles ; 
  private Queue<LauncherDestroyer> destroyers; 
  private Queue<IronDome> ironDomes; 
  private Queue<EnemyLauncher> launchers ;

  public void init(){
        war = new War();    

  }

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    doGetAndPost(request, response);

  }

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    doGetAndPost(request, response);
  }

  private void doGetAndPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException  {


    PrintWriter out = response.getWriter();
    try {

      if(enterOnce){
//        if(request.getParameter("war") == null){             
//          war = new War();    
          missiles = war.getAllMissiles(); 
          destroyers = war.getWarLauncherDestroyer(); 
          ironDomes = war.getIronDomes(); 
          launchers = war.getLaunchers();
          enterOnce = false;

//        }
        enterOnce = false;
      }
      response.setContentType("text/html;charset=UTF-8");

      // button was clicked
      if (request.getParameter("B") != null) {
        String id = request.getParameter("A");
        war.Create_enemy_launcher(id);
        out.append("<html> " +  id + " added successfully" + "</html>");
        out.println("\n<a href=\"/WarGameServlet/WarHtml.html\"> <input type=\"submit\" value=\"Return\"></a>");
      }
      else if(request.getParameter("D") != null){
        String ironDomeiD = request.getParameter("C");
        war.Create_Iron_Dome(ironDomeiD);
        out.append("<html> " +  ironDomeiD + " added successfully" + "</html>");
        out.println("\n<a href=\"/WarGameServlet/WarHtml.html\"> <input type=\"submit\" value=\"Return\"></a>");

      }
      else if(request.getParameter("K") != null){
        String type =  request.getParameter("destroyerType");

        out.println("<html>");
     //   out.println("<form name=\"selectLaunchersPage\" action=\"/WarGameServlet/WarHtml.html\" method=\"GET\">");
        out.println("<p>Select launcher to destroy: </p>");
        for (EnemyLauncher launcher : launchers){
          out.println( "<p>" + launcher.getLauncherId() +"</p>");
        }
        out.println("<p>Select Destructor of type:</p>" + type );
        for (LauncherDestroyer destroyer : destroyers){
          out.println("<p>" + destroyer.getLauncherId() + "</p>" );
        }
        out.println("<p>Select Missile to destroy: </p>");
        for (EnemyMissile missile : missiles){
          out.println("<p>" + missile.getID()  + "</p>");
        }
        out.println("<p>Select Iron Dome to shoot from </p>");
        for (IronDome ironD : ironDomes){
          out.println("<p>" + ironD.getDomeId() + "</p>");
        }
        out.println("<p><a href=\"/WarGameServlet/WarHtml.html\"> <input type=\"submit\" value=\"Return\"></a></p>");
     //   out.println("</form>");
        out.println("</html>");
        enterOnce = true;
      }
      else if(request.getParameter("G") != null){
        String launcheID =  request.getParameter("E");
        String destructorID =  request.getParameter("F");
        int id = Integer.parseInt(destructorID);
        war.DestroyLauncher(5+"", launcheID, war.findDestroyerById(id));
        out.append("<html> " +  destructorID + " Processing" + "</html>");
        out.println("\n<a href=\"/WarGameServlet/WarHtml.html\"> <input type=\"submit\" value=\"Return\"></a>");

      }
      else if(request.getParameter("J") != null){
        String missileID =  request.getParameter("H");
        String domeID =  request.getParameter("I");
        war.InterceptMissile(5+"", missileID, war.findIronDomeById(domeID));
        out.append("<html> " +  domeID + " Processing" + "</html>");
        out.println("\n<a href=\"/WarGameServlet/WarHtml.html\"> <input type=\"submit\" value=\"Return\"></a>");

      }
 
    } finally {
      out.close();
    }
  }
}
