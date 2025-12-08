package Lakshmi.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.UUID;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.methods=POST",
                "sling.servlet.paths=/bin/contact/formdata"
        }
)
public class ContactFormServlet extends SlingAllMethodsServlet {

    private static final String CONTACT_ROOT = "/content/userdata/Contacts";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            handleCreate(request, response);
        } else if ("update".equals(action)) {
            handleUpdate(request, response);
        } else if ("delete".equals(action)) {
            handleDelete(request, response);
        } else {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void handleCreate(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        ResourceResolver resolver = request.getResourceResolver();
        try {
            Resource rootResource = resolver.getResource(CONTACT_ROOT);
            if (rootResource == null) {
                response.getWriter().write("Root path not found");
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            String nodeName = "contact-" + UUID.randomUUID();
            ModifiableValueMap mvm = resolver.create(rootResource, nodeName,
                            java.util.Collections.singletonMap("jcr:primaryType", "nt:unstructured"))
                    .adaptTo(ModifiableValueMap.class);

            mvm.put("name", name);
            mvm.put("email", email);
            mvm.put("mobile", mobile);

            resolver.commit();
            response.getWriter().write("Contact created");
        } catch (PersistenceException e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleUpdate(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {

        String nodePath = request.getParameter("nodePath");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        ResourceResolver resolver = request.getResourceResolver();
        try {
            Resource contactRes = resolver.getResource(nodePath);
            if (contactRes == null) {
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ModifiableValueMap mvm = contactRes.adaptTo(ModifiableValueMap.class);
            mvm.put("name", name);
            mvm.put("email", email);
            mvm.put("mobile", mobile);

            resolver.commit();
            response.getWriter().write("Contact updated");
        } catch (PersistenceException e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleDelete(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {

        String deletePath = request.getParameter("deletePath");
        ResourceResolver resolver = request.getResourceResolver();

        try {
            Resource contactRes = resolver.getResource(deletePath);
            if (contactRes == null) {
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                return;
            }

            resolver.delete(contactRes);
            resolver.commit();
            response.getWriter().write("Contact deleted");
        } catch (PersistenceException e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
