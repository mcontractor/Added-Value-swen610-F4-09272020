package com.my_pls.demo;

import com.mongodb.*;

import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.net.URLDecoder;

import static spark.Spark.*;



public class App {
    static int userKey = 001; //used as a database key for users

    private static Map<String,String> extractFields(String body){
        try{
            Map<String,String> map = new HashMap<>(0);
            String[] pairs =  body.split("&");
            for (String pair : pairs){
                String[] keyAndValue = pair.split("=");
                map.put(keyAndValue[0],keyAndValue[1]);
            }

            return map;
        }catch (Exception ex){
            return null;
        }

    }
//    Map<String,String> map = extractFields(request.body());
//        return map;

    public static void main(String[] args) throws IOException {

        port(8080);

        MongoClient mongoClient = new MongoClient();
        //MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        //MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("myPLS");//cheange from test when done inputting garbage data
        DBCollection collection = database.getCollection("users");



        final TemplateEngine engine = new FreeMarkerEngine();
        internalServerError("<html><body>Something went wrong!</body></html>");
        staticFileLocation("/public"); //So that it has access to the pubic resources(stylesheets, etc.)

        CurrUser user_current = new CurrUser();


//        mAuth.createUser(user_request);
//        UserRecord user = mAuth.getUser("TG4b6yMyPYaHiW2yYpyBfU6lAki2");
//        System.out.println(user.getEmail());

        post("/sub", ((request, response) -> {

            Map<String,String> map = extractFields(request.body());
            return map;
        }
        ));

        // Setting any route (or filter) in Spark triggers initialization of the embedded Jetty web server.
        get("/", (request, response) -> {return new ModelAndView(new HashMap<>(),"sub.ftl");},engine);
        get("/hello/:name",(request, response) -> {
            String name = request.params(":name");
            Map<String,Object> map = new HashMap<>();
            map.put("title",name);
            return new ModelAndView(map,"home.ftl");
        },engine);

        get("/login",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("actionLink", "/login");
            map.put("errorEmail", "");
            map.put("errorPassMatch", "");
            map.put("loginErr", "");
            map.put("emailVal","");
            map.put("pageType","Login");
            map.put("styleVal", "margin-top:12%; width:45%");
            return new ModelAndView(map,"login.ftl");
        },engine);

        post("/login",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());

            if (formFields.size() > 0) {
                if (!formFields.get("email").contains("rit.edu")) {
                    map.put("errorEmail", "display:list-item;margin-left:5%");
                    map.put("emailVal","");
                    map.put("loginErr", "");
                } else {
                    String emVal = formFields.get("email");
                    try {
                        emVal = URLDecoder.decode(emVal, "UTF-8");
                    } catch (Exception e) {}
//                    try {
//                        UserRecord login_user = mAuth().
////                        String password = login_user.
//                    }
                    map.put("loginErr", "display:list-item;margin-left:5%");
                    map.put("errorEmail", "");

                    map.put("emailVal",emVal);
                }
            } else {
                map.put("loginErr", "");
                map.put("emailVal", "");
            }
            map.put("actionLink", "/login");
            map.put("errorPassMatch", "");
            map.put("pageType","Login");
            map.put("styleVal", "margin-top:12%; width:45%");
            return new ModelAndView(map,"login.ftl");
        },engine);

        get("/register",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("actionLink", "/register");
            map.put("loginErr", "");
            map.put("errorEmail", "");
            map.put("errorPassMatch", "");
            map.put("fname","");
            map.put("lname","");
            map.put("emailVal","");
            map.put("pageType","Register");
            map.put("styleVal", "margin-top:5%; width:45%");
            return new ModelAndView(map,"login.ftl");
        },engine);

        post("/register",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            map.put("actionLink", "/register");
            map.put("loginErr", "");
            if (formFields.size() > 0) {
                Boolean flag = true;
                if (!formFields.get("email").contains("rit.edu")) {
                    map.put("errorEmail", "display:list-item;margin-left:5%");
                    map.put("emailVal","");
                    flag = false;
                } else {
                    map.put("errorEmail", "");
                    String emVal = formFields.get("email");
                    try {
                        emVal = URLDecoder.decode(emVal, "UTF-8");
                    } catch (Exception e) {}
                    map.put("emailVal",emVal);
                }
                if (formFields.get("pass").equals(formFields.get("retPass")) && formFields.get("pass").length() >= 6) {
                    map.put("errorPassMatch", "");
                } else {
                    flag = false;
                    map.put("errorPassMatch", "display:list-item;margin-left:5%");
                }
                if (flag) {

                    String email = formFields.get("email");
                    email = URLDecoder.decode(email,"UTF-8");


                    String display_name = formFields.get("fname") +" " +  formFields.get("lname");

                    user_current.firstName = formFields.get("fname");
                    user_current.lastName = formFields.get("lname");
                    user_current.email = formFields.get("email");
                    user_current.password = formFields.get("pass");

                    DBObject user = new BasicDBObject("_id", userKey)
                            .append("fname", formFields.get("fname"))
                            .append("lname", formFields.get("lname"))
                            .append("email", email)
                            .append("password",user_current.password);//store as hash
                    userKey++;
                    collection.insert(user);
                    response.redirect("/verify-register");
                } else {
                    map.put("fname",formFields.get("firstName"));
                    map.put("lname",formFields.get("lastName"));
                }
            }
            map.put("pageType","Register");
            map.put("styleVal", "margin-top:5%; width:45%");
            return new ModelAndView(map,"login.ftl");
        },engine);

        get("/verify-register",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"verifyRegister.ftl");
        }),engine);

        get("/forgot-password/:type",((request, response) -> {
            String pageType = request.params(":type");
            Map<String,String> map = new HashMap<>();
            map.put("pageType", pageType);
            map.put("actionLink", ("/forgot-password/" + pageType));
            map.put("success", "false");
            map.put("succMsg", "");
            map.put("errorEmail", "");
            map.put("errorPassMatch", "");
            return new ModelAndView(map,"forgotPassword.ftl");
        }),engine);

        post("/forgot-password/:type",((request, response) -> {
            String pageType = request.params(":type");
            Map<String,String> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
           if (pageType.equals("email")) {
               map.put("errorPassMatch", "");
               if (!formFields.get("email").contains("rit.edu")) {
                   map.put("errorEmail", "display:block;margin-left:5%; width:90%");
                   map.put("emailVal","");
                   map.put("success", "false");
                   map.put("succMsg", "");
               } else {
                   map.put("errorEmail", "");
                   map.put("success", "true");
                   map.put("succMsg", "A verification link has been emailed to you!");
               }
           } else {
               map.put("errorEmail", "");
               if (formFields.get("pass").equals(formFields.get("retPass")) && formFields.get("pass").length() >= 6) {
                   map.put("errorPassMatch", "");
                   map.put("success", "true");
                   map.put("succMsg", "Your password has been changed. Please log in again.");
               } else {
                   map.put("errorPassMatch", "display:block;margin-left:5%; width:90%");
                   map.put("success", "false");
                   map.put("succMsg", "");
               }
           }

            map.put("pageType", pageType);
            map.put("actionLink", ("/forgot-password/" + pageType));
            return new ModelAndView(map,"forgotPassword.ftl");
        }),engine);

        get("/first/*/last/*",(request, response) -> {

            String firstName = request.splat()[0];
            String lastName = request.splat()[1];
            Map<String,Object> map = new HashMap<>();
            map.put("title",String.format("%s %s",firstName,lastName));
            return new ModelAndView(map,"home.ftl");
        },engine);

        get("/home",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

        get("/course/about",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "prof");
            return new ModelAndView(map,"courseAbout.ftl");
        }),engine);

        get("/course/learnMat",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "prof");
            return new ModelAndView(map,"courseLearnMat.ftl");
        }),engine);

        get("/course/quiz",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "prof");
            return new ModelAndView(map,"courseQuiz.ftl");
        }),engine);

        get("/course/grade",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "prof");
            return new ModelAndView(map,"courseGrade.ftl");
        }),engine);

        get("/course/classlist",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "prof");
            return new ModelAndView(map,"courseClasslist.ftl");
        }),engine);

        get("/course/rate",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"courseRate.ftl");
        }),engine);

        get("/courses",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"courses.ftl");
        }),engine);

        get("/course/create-quiz",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"createQuiz.ftl");
        }),engine);

        get("/enroll",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"enroll.ftl");
        }),engine);

        post("/enroll",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            System.out.println(formFields);
            return new ModelAndView(map,"enroll.ftl");
        }),engine);

        get("/enroll/about",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"enrollAbout.ftl");
        }),engine);

        Pokemon charmander  = new Pokemon();
        charmander.name = "Charry";
        charmander.type = "fire";

        Pokemon rattata = new Pokemon();
        rattata.name = "ratbi";
        rattata.type = "grass";

        get("/list",(request, response) -> {

            List<Pokemon> pokeList = new ArrayList<>(4);
            pokeList.add(charmander);
            pokeList.add(rattata);

            Map<String,Object> map = new HashMap<>();
            map.put("pokemon",pokeList);
            return new ModelAndView(map,"example.ftl");

        },engine);

        path("/user",()->{
            get("/",(req,res)-> req.session().attribute("name"));
            get("/update/:name",(req,res)->{
                String name = req.params(":name");
                req.session().attribute("name",name);
                return String.format("Value updated with %s",name);
            });

        });

    }

    public static class CurrUser {
        String firstName;
        String lastName;
        String password;
        String email;
    }

    public static class Pokemon{
        String name;
        String type;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }
}
