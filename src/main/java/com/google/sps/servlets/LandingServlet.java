// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.PrintWriter;
import java.io.IOException;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@WebServlet("/landing")
public class LandingServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json"); 
    UserService userService = UserServiceFactory.getUserService();
    System.out.println(userService.isUserLoggedIn());

    if (!userService.isUserLoggedIn()) {
        String urlToRedirectToAfterUserLogsIn = "/timeline.html";
        String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
        System.out.println(loginUrl);
        Gson gson = new Gson();
        String responseBody = gson.toJson(loginUrl);
        response.getWriter().println(responseBody);
        return;
    } 
    else {
        String urlToRedirectToAfterUserLogsOut = "/index.html";
        String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
        System.out.println(logoutUrl);
        Gson gson = new Gson();
        String responseBody = gson.toJson(logoutUrl);
        response.getWriter().println(responseBody);
        return;
    }
  }

  private boolean hasBiography(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
    Query query =
        new Query("UserBiography")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
        return false; 
    }
    return true;       
  }

}