package me.efjerryyang.webserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import me.efjerryyang.webserver.model.*;
import me.efjerryyang.webserver.service.*;
import me.efjerryyang.webserver.util.CryptoUtilHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class SignupController {
    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);
    private final UserService userService;
    private final MerchantService merchantService;
    private final UserRoleService userRoleService;
    private final MerchantUserService merchantUserService;
    private final ValidationService validationService;
    private User user = null;
    private Role role = null;
    private UserRole userRole = null;
    private Merchant merchant = null;
    private MerchantUser merchantUser = null;

    public SignupController(UserService userService, MerchantService merchantService, UserRoleService userRoleService, MerchantUserService merchantUserService, ValidationService validationService) {
        this.userService = userService;
        this.merchantService = merchantService;
        this.userRoleService = userRoleService;
        this.merchantUserService = merchantUserService;
        this.validationService = validationService;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        logger.info("SignupController.signup() called");
        logger.info("Model is: " + model);
        return "signup";
    }

    @PostMapping("/signup_staff")
    public String signupStaff(@RequestParam(value = "username", defaultValue = "") String username,
                              @RequestParam(value = "password", defaultValue = "") String password,
                              @RequestParam(value = "phone", defaultValue = "") String phone,
                              @RequestParam(value = "email", defaultValue = "") String email,
                              @RequestParam(value = "firstname", defaultValue = "") String firstname,
                              @RequestParam(value = "lastname", defaultValue = "") String lastname,
                              @RequestParam(value = "idNumber", defaultValue = "") String idNumber,
                              @RequestParam(value = "jobTitle", defaultValue = "") String jobTitle,
                              @RequestParam(value = "company", defaultValue = "") String company, Model model) {
        logger.info("username: {} password: {} phone: {} email: {} firstname: {} lastname: {} idNumber: {} jobTitle: {} company: {}", username, password, phone, email, firstname, lastname, idNumber, jobTitle, company);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String acceptHeader = request.getHeader("Accept");
        String jsEnabled = request.getParameter("jsEnabled");
        // validate if javascript is enabled
        if (!validationService.isJavascriptEnabled(acceptHeader, jsEnabled)) {
            logger.info("Validating staff signup form on server side");
            if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || idNumber.isEmpty() || jobTitle.isEmpty() || company.isEmpty()) {
                logger.info("Staff signup form is not valid");
                model.addAttribute("error", "Please fill in all the fields");
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                model.addAttribute("phone", phone);
                model.addAttribute("email", email);
                model.addAttribute("firstname", firstname);
                model.addAttribute("lastname", lastname);
                model.addAttribute("idNumber", idNumber);
                model.addAttribute("jobTitle", jobTitle);
                model.addAttribute("company", company);
                return "signup_staff";
            }
            if (!validationService.isUsername(username) || !validationService.isPhone(phone) || !validationService.isEmail(email) || !validationService.isName(firstname) || !validationService.isName(lastname) || !validationService.isIdNumber(idNumber) || !validationService.isJobTitle(jobTitle) || !validationService.isCompany(company)) {
                logger.info("Staff signup form is not valid");
                if (!validationService.isUsername(username)) {
                    model.addAttribute("error", "Username is not valid");
                    logger.info("Username is not valid");
                } else if (!validationService.isPhone(phone)) {
                    model.addAttribute("error", "Phone is not valid");
                    logger.info("Phone is not valid");
                } else if (!validationService.isEmail(email)) {
                    model.addAttribute("error", "Email is not valid");
                    logger.info("Email is not valid");
                } else if (!validationService.isName(firstname)) {
                    model.addAttribute("error", "Firstname is not valid");
                    logger.info("Firstname is not valid");
                } else if (!validationService.isName(lastname)) {
                    model.addAttribute("error", "Lastname is not valid");
                    logger.info("Lastname is not valid");
                } else if (!validationService.isIdNumber(idNumber)) {
                    model.addAttribute("error", "Id number is not valid");
                    logger.info("Id number is not valid");
                } else if (!validationService.isJobTitle(jobTitle)) {
                    model.addAttribute("error", "Job title is not valid");
                    logger.info("Job title is not valid");
                } else if (!validationService.isCompany(company)) {
                    model.addAttribute("error", "Company is not valid");
                    logger.info("Company is not valid");
                }
                model.addAttribute("username", username);
                logger.info("username: {}", username);
                model.addAttribute("password", password);
                logger.info("password: {}", password);
                model.addAttribute("phone", phone);
                logger.info("phone: {}", phone);
                model.addAttribute("email", email);
                logger.info("email: {}", email);
                model.addAttribute("firstname", firstname);
                logger.info("firstname: {}", firstname);
                model.addAttribute("lastname", lastname);
                logger.info("lastname: {}", lastname);
                model.addAttribute("idNumber", idNumber);
                logger.info("idNumber: {}", idNumber);
                model.addAttribute("jobTitle", jobTitle);
                logger.info("jobTitle: {}", jobTitle);
                model.addAttribute("company", company);
                logger.info("company: {}", company);
                return "signup_staff";
            }
        }
        if (merchant != null) {
            merchant = null;
        }
        if (merchantUser != null) {
            merchantUser = null;
        }
        if (user == null) {
            throw new RuntimeException("User is null");
        }
        merchant = new Merchant();
        merchantUser = new MerchantUser();
        try {
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setIdNumber(idNumber);
            merchant.setIsActive(false);
            merchant.setMerchantId(merchantService.getNextId());
            // set username as merchant name is not appropriate
            merchant.setName(user.getUsername()); // should be done when verified
            merchantUser.setUserId(user.getUserId());
            merchantUser.setMerchantId(merchant.getMerchantId());
            merchantUser.setJobTitle(jobTitle);
            merchantUser.setCompany(company);
            merchantUser.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
        }
        try {
            userService.create(user);
            role = Role.createRole("default");  // temporary solution, maybe use a map instead
            userRole = userRoleService.bindUserAndRole(user.getUserId(), role.getRoleId());
            userRoleService.create(userRole);

            merchantService.create(merchant);
            merchantUser = merchantUserService.bindUserAndMerchant(user.getUserId(), merchant.getMerchantId());
            merchantUserService.create(merchantUser);

        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
        }

        return "redirect:/notice_staff";
    }

    @PostMapping("/signup_admin")
    public String signupAdmin(@RequestParam(value = "username", defaultValue = "") String username,
                              @RequestParam(value = "password", defaultValue = "") String password,
                              @RequestParam(value = "phone", defaultValue = "") String phone,
                              @RequestParam(value = "email", defaultValue = "") String email,
                              @RequestParam(value = "firstname", defaultValue = "") String firstname,
                              @RequestParam(value = "lastname", defaultValue = "") String lastname,
                              @RequestParam(value = "address", defaultValue = "") String address,
                              @RequestParam(value = "referenceName", defaultValue = "") String referenceName,
                              @RequestParam(value = "referenceContact", defaultValue = "") String referenceContact,
                              Model model) {
        logger.info("username: {} password: {} phone: {} email: {} firstname: {} lastname: {} address: {} referenceName: {} referenceContact {}", username, password, phone, email, firstname, lastname, address, referenceName, referenceContact);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String acceptHeader = request.getHeader("Accept");
        String jsEnabled = request.getParameter("jsEnabled");
        // validate if javascript is enabled
        if (!validationService.isJavascriptEnabled(acceptHeader, jsEnabled)) {
            logger.info("Validating admin signup form on server side");
            if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || address.isEmpty() || referenceName.isEmpty() || referenceContact.isEmpty()) {
                logger.info("Admin signup form is not valid");
                model.addAttribute("error", "Please fill in all the fields");
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                model.addAttribute("phone", phone);
                model.addAttribute("email", email);
                model.addAttribute("firstname", firstname);
                model.addAttribute("lastname", lastname);
                model.addAttribute("address", address);
                model.addAttribute("referenceName", referenceName);
                model.addAttribute("referenceContact", referenceContact);
                return "signup_admin";
            }
            if (!validationService.isUsername(username) || !validationService.isPhone(phone) || !validationService.isEmail(email) || !validationService.isName(firstname) || !validationService.isName(lastname) || !validationService.isAddress(address) || !validationService.isName(referenceName) || !validationService.isPhone(referenceContact)) {
                logger.info("Admin signup form is not valid");
                if (!validationService.isUsername(username)) {
                    model.addAttribute("error", "Username is not valid");
                    logger.info("Username is not valid");
                } else if (!validationService.isPhone(phone)) {
                    model.addAttribute("error", "Phone is not valid");
                    logger.info("Phone is not valid");
                } else if (!validationService.isEmail(email)) {
                    model.addAttribute("error", "Email is not valid");
                    logger.info("Email is not valid");
                } else if (!validationService.isName(firstname)) {
                    model.addAttribute("error", "Firstname is not valid");
                    logger.info("Firstname is not valid");
                } else if (!validationService.isName(lastname)) {
                    model.addAttribute("error", "Lastname is not valid");
                    logger.info("Lastname is not valid");
                } else if (!validationService.isAddress(address)) {
                    model.addAttribute("error", "Address is not valid");
                    logger.info("Address is not valid");
                } else if (!validationService.isName(referenceName)) {
                    model.addAttribute("error", "Reference name is not valid");
                    logger.info("Reference name is not valid");
                } else if (!validationService.isPhone(referenceContact) && !validationService.isEmail(referenceContact)) {
                    model.addAttribute("error", "Reference contact is not valid");
                    logger.info("Reference contact is not valid");
                }
                model.addAttribute("username", username);
                logger.info("username: {}", username);
                model.addAttribute("password", password);
                logger.info("password: {}", password);
                model.addAttribute("phone", phone);
                logger.info("phone: {}", phone);
                model.addAttribute("email", email);
                logger.info("email: {}", email);
                model.addAttribute("firstname", firstname);
                logger.info("firstname: {}", firstname);
                model.addAttribute("lastname", lastname);
                logger.info("lastname: {}", lastname);
                model.addAttribute("address", address);
                logger.info("address: {}", address);
                model.addAttribute("referenceName", referenceName);
                logger.info("referenceName: {}", referenceName);
                model.addAttribute("referenceContact", referenceContact);
                logger.info("referenceContact: {}", referenceContact);
                return "signup_admin";
            }
        }
        if (user == null) {
            throw new RuntimeException("User is null");
        }
        if (role != null) {
            role = null;
        }

        try {
            user.setAddress(address);
            user.setFirstname(firstname);
            user.setLastname(lastname);
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
        }

        try {
            userService.create(user);
            role = Role.createRole("default");  // temporary solution, maybe use a map instead
            userRole = userRoleService.bindUserAndRole(user.getUserId(), role.getRoleId());
            userRoleService.create(userRole);
            // TODO: notify reference by name and contact, then update the role from default to admin
            logger.info("Notifying reference by name {} and contact {}", referenceName, referenceContact);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
        }
        return "redirect:/notice_admin";
    }

    @PostMapping("/signup")
    public String handleSignupForm(@RequestParam(value = "username", defaultValue = "") String username,
                                   @RequestParam(value = "password", defaultValue = "") String password,
                                   @RequestParam(value = "phone", defaultValue = "") String phone,
                                   @RequestParam(value = "email", defaultValue = "") String email,
                                   @RequestParam(value = "options", defaultValue = "") String options, Model model) {
        logger.info("username: {} password: {} phone: {} email: {} options: {}", username, password, phone, email, options);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String acceptHeader = request.getHeader("Accept");
        String jsEnabled = request.getParameter("jsEnabled");
        // validate if javascript is enabled
        if (!validationService.isJavascriptEnabled(acceptHeader, jsEnabled)) {
            logger.info("Validating signup form data from server side");
            // validate the form input
            if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty() || options.isEmpty()) {
                logger.info("SignupController.handleSignupForm() called with empty fields");
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                model.addAttribute("phone", phone);
                model.addAttribute("email", email);
                model.addAttribute("options", options);
                model.addAttribute("error", "Please fill out all the fields");
                return "signup";
            }
            if (!validationService.isUsername(username) || !validationService.isPhone(phone) || !validationService.isEmail(email) || !validationService.isRole(options)) {
                logger.info("SignupController.handleSignupForm() called with invalid fields");
                if (!validationService.isUsername(username)) {
                    model.addAttribute("error", "Username is not valid");
                    logger.info("Username is not valid");
                } else if (!validationService.isPhone(phone)) {
                    model.addAttribute("error", "Phone is not valid");
                    logger.info("Phone is not valid");
                } else if (!validationService.isEmail(email)) {
                    model.addAttribute("error", "Email is not valid");
                    logger.info("Email is not valid");
                } else if (!validationService.isRole(options)) {
                    model.addAttribute("error", "Role is not valid");
                    logger.info("Role is not valid");
                }
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                model.addAttribute("phone", phone);
                model.addAttribute("email", email);
                model.addAttribute("options", options);
                return "signup";
            }
            // compute the sha256 hash of the password
            password = CryptoUtilHash.hash(password);
            logger.info("hashed password: {}", password);
        }
        if (user != null) {
            user = null;
        }
        if (role != null) {
            role = null;
        }
        user = new User();
        String salt = CryptoUtilHash.getSalt();
        user.setSalt(salt);
        user.setUsername(username);
        user.setPassword(CryptoUtilHash.hashWithSalt(password, salt));
        user.setPhone(phone);
        user.setEmail(email);
        user.setUserId(userService.getNextId());
        user.setIsActive(true);
        user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        logger.info(String.valueOf(user));
        switch (options) {
            case "customer":
                try {
                    userService.create(user);
                    role = Role.createRole("customer");  // temporary solution, maybe use a map instead
                    userRole = userRoleService.bindUserAndRole(user.getUserId(), role.getRoleId());
                    userRoleService.create(userRole);
                } catch (Exception e) {
                    logger.error("Error creating user: {}", e.getMessage());
                    model.addAttribute("username", username);
                    model.addAttribute("password", password); // hashed password
                    model.addAttribute("phone", phone);
                    model.addAttribute("email", email);
                    model.addAttribute("options", options);
                    model.addAttribute("error", "Error creating user: " + e.getMessage());
                    return "signup";
                }
                return "redirect:/notice_customer";
            case "admin":
                model.addAttribute("username", username);
                model.addAttribute("password", password); // hashed password
                model.addAttribute("phone", phone);
                model.addAttribute("email", email);
                return "signup_admin";
            case "staff":
                model.addAttribute("username", username);
                model.addAttribute("password", password); // hashed password
                model.addAttribute("phone", phone);
                model.addAttribute("email", email);
                return "signup_staff";
            default:
                return "redirect:/welcome";
        }
    }
}