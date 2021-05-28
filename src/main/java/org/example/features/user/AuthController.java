package org.example.features.user;

import org.example.core.Conf;
import org.example.core.Database;
import org.example.core.Template;
import org.example.models.User;
import org.example.utils.SessionUtils;
import org.example.utils.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.Spark;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserDao userDao = new UserDao();

    public String login(Request request, Response response) {
        if (request.requestMethod().equals("GET")) {
            Map<String, Object> model = new HashMap<>();
            return Template.render("auth_login.html", model);
        }

        Map<String, String> query = URLUtils.decodeQuery(request.body());
        String email = query.get("email");
        String password = query.get("password");

        // Authenticate user
        User user = userDao.getUserByCredentials(email, password);
        if (user == null) {
            logger.info("User not found. Redirect to login");
            response.removeCookie("session");
            response.redirect(Conf.ROUTE_LOGIN);
            return "KO";
        }

        // Create session
        SessionUtils.createSession(user, request, response);

        // Redirect to medias page
        response.redirect(Conf.ROUTE_AUTHENTICATED_ROOT);
        return null;
    }

    public String signUp(Request request, Response response) {
        if (request.requestMethod().equals("GET")) {
            Map<String, Object> model = new HashMap<>();
            return Template.render("auth_signup.html", model);
        }

        Map<String, String> query = URLUtils.decodeQuery(request.body());
        String email = query.get("email");
        String username = query.get("username");
        String password = query.get("password");

        Connection connection = Database.get().getConnection();

        try {
            Statement st = connection.createStatement();
            int r = st.executeUpdate("INSERT INTO discoding.users (email, username, password)" +
                    "VALUES('" + email + "', '" + username + "','" + password + "');");
            System.out.println(r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // mapping for email, passowrd and confirm
        //faire la connection par connection et preparedstatement pour l'ordre sql
        //redirect to the login page afterward
        response.redirect(Conf.ROUTE_LOGIN);
        return null;
    }

    public String logout(Request request, Response response) {
        Session session = request.session(false);
        if (session == null) {
            Spark.halt(401, "No valid session found");
            return ("KO");
        }
        session.invalidate();
        response.removeCookie("user_id");
        response.redirect("/");
        return null;
    }

    public String contactUs(Request request, Response response) {
        if (request.requestMethod().equals("GET")) {
            Map<String, Object> model = new HashMap<>();
            return Template.render("contact_us.html", model);
        }
        return null;
    }
}
