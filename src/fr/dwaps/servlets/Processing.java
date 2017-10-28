package fr.dwaps.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.dwaps.beans.User;
import fr.dwaps.utils.Constants;

public class Processing extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private HttpSession session;
       
    public Processing() {
        super();
    }
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		super.service(request, response);
    		session = request.getSession();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext();
		RequestDispatcher dispatcher = context.getNamedDispatcher("Home");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		createUser(request);
		doGet(request, response);
	}
	
	private void createUser(HttpServletRequest request) {
		User user = new User();
		
		// Récupération des valeurs des champs
		String firstname = request.getParameter(Constants.INPUT_FIRSTNAME);
		String lastname = request.getParameter(Constants.INPUT_LASTNAME);
		int age = -1; // Par défaut, un âge improbable...
		try {
			age = Integer.parseInt(request.getParameter(Constants.INPUT_AGE));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("La valeur du champ age n'a pas pu être convertie en int.");
		}
		
		// Vérification des données récupérées
		boolean firstnameIsOk = checkName(firstname);
		boolean lastnameIsOk = checkName(lastname);
		boolean ageIsOk = (age > 0 && age < 130);
		
		// Si au moins une valeur n'est pas correct, on va pas plus loin...
		if (!firstnameIsOk || !lastnameIsOk || !ageIsOk) {
			return;
		}
		
		// ... sinon on peut créer notre user
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setAge(age);
		
		// On ajoute l'user à la session pour le rendre disponible
		session.setAttribute("user", user);
	}
	
	private boolean checkName(String name) {
		return (null != name && !name.isEmpty() && name.length() > 1 && name.length() < 50);
	}
}
