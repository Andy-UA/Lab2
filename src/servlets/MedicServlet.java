package servlets;

import com.google.gson.Gson;
import entities.MedicEntity;
import services.DrugService;
import services.MedicService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Andrew on 29.03.2017.
 */
@WebServlet(name = "MedicServlet")
public class MedicServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String jsonInString = buffer.toString();
        Gson gson = new Gson();
        MedicEntity u = gson.fromJson(jsonInString, MedicEntity.class);
        new MedicService().add(u);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("action"));
        new DrugService().delete(id);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Gson gson = new Gson();

        switch (request.getParameter("action")){
            case "getAll": {
                List<MedicEntity> de = new MedicService().getAll();
                String result = "[";
                for (int i = 0; i < de.size(); i++) {
                    if (i != 0) {
                        result += ",";
                    }
                    result += gson.toJson(de.get(i));
                }
                result += "]";
                response.getWriter().write(result);
                break;
            }
            case"find": {
                long id = Long.parseLong(request.getParameter("id"));
                MedicEntity de = new MedicService().get(id);
                String result = gson.toJson(de);
                response.getWriter().write(result);
                break;
            }
        }
    }
}
