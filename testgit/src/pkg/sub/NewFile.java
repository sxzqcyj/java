/**
 * 
 */
package pkg.sub;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.w3c.dom.Document;

//import net.sf.json.JSONException;
//import net.sf.json.JSONObject;

/**
 * @author cyj-pc
 *
http://docs.oracle.com/cloud/latest/fmw122100/RESTF/deploy-restful-service.htm#RESTF191
 <web-app>
   <servlet>
        <servlet-name>pkg.sub.NewFile</servlet-name>
    </servlet>
    <servlet-mapping>
        <servlet-name>pkg.sub.rest.NewFile</servlet-name>
        <url-pattern>/rest</url-pattern>
    </servlet-mapping>
    ... 或 ...
    <servlet>
        <servlet-name>Jersey Web Application</servlet-name>
        <servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
	 	<init-param>
		   <param-name>
		      javax.ws.rs.Application
		   </param-name>
		   <param-value>
		      pkg.sub.NewFile
		   </param-value>
		</init-param>
		<init-param> 
	   <param-name>
	      jersey.config.server.provider.packages
	   </param-name> 
	   <param-value>
	      pkg.sub.NewFile
	    </param-value> 
	</init-param> 
	<init-param>
	    <param-name>
	       jersey.config.server.provider.scanning.recursive
	    </param-name>
	    <param-value>
	       false
	    </param-value>
	</init-param> 
        <init-param>
           <param-name>jersey.config.server.provider.packages</param-name>
           <param-value>org.foo.myresources,org.bar.otherresources</param-value>
        </init-param>
        <init-param>
           <param-name>jersey.config.server.provider.scanning.recursive</param-name>
           <param-value>false</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>Jersey Web Application</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
   <security-constraint>
         <web-resource-collection>
             <web-resource-name>Orders</web-resource-name>
             <url-pattern>/orders</url-pattern>
             <http-method>GET</http-method>
             <http-method>POST</http-method>
         </web-resource-collection>
         <auth-constraint>
             <role-name>admin</role-name> 
         </auth-constraint>
    </security-constraint>
    <login-config>
        <auth-method>BASIC</auth-method>
        <realm-name>default</realm-name>
    </login-config>
    <security-role>
        <role-name>admin</role-name>
    </security-role>    
</web-app>


http://<host>:<port>/<contextPath>/rest/helloworld
 */
@Path("/helloworld")
@ApplicationPath("rest")
public class NewFile  extends Application {
	 
	public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(NewFile.class);
        return s;
    }
	/**
	 * 
	 */
	public NewFile() {
		System.out.println("new pkg.sub.NewFile()");
	}
	 @GET
   @Produces("text/plain")
   @Path("/say/{username: [a-zA-Z][a-zA-Z_0-9]}")
   @Encoded 
   public String sayHello(@PathParam("username") String username) {
      return "Hello World!"+username;
   }
	@Context 
	UriInfo uriInfo;
	
	@GET
    @Path("/list")
	@Produces("text/plain;charset=UTF-8")
	@RolesAllowed({"ADMIN", "ORG1"})
    public String getUserList(@Context SecurityContext sc,@DefaultValue("2") @QueryParam("step") int step ) {
		return "Hello World list!"+step+sc.isUserInRole("admin");
   }

	   @GET
	   @Path("/html/{id}")
	   public Response getHTMLDoc(@PathParam("id") int docId, @Context Request req)
	   { 
	      Document document = null;
	      List<String> list = new ArrayList<String>();
	      GenericEntity<List<String>> entity = new GenericEntity<List<String>>(list) {};
          ResponseBuilder response = Response.ok(document);
	      response.entity(entity);
	      response.type("text/html");
	      
		 EntityTag tag = computeEntityTag(uriInfo.getRequestUri());
 		 Response.ResponseBuilder rb = req.evaluatePreconditions(tag);
		 rb = Response.ok();
         rb.tag(tag);
         if (!req.getMethod().equals("GET"))
            return rb.build();
         
		 return response.build();
	   }
	    private EntityTag computeEntityTag(URI requestUri) {
		// TODO Auto-generated method stub
		return null;
	}
		public static void main(String[] args) {
	        Client client = ClientBuilder.newClient();
	        WebTarget target = client.target("http://localhost:7101/restservice");
	        WebTarget resourceWebTarget;
	        resourceWebTarget = target.path("resources/helloworld");
	        Invocation.Builder invocationBuilder;
	        invocationBuilder = resourceWebTarget.request(
	          MediaType.TEXT_PLAIN_TYPE);
	        Response response = invocationBuilder.get();
	        System.out.println(response.getStatus());
	        System.out.println(response.readEntity(String.class));
	    }	  
//	    @PUT
//	    @Consumes(MediaType.APPLICATION_JSON)
//	    public void putBookmark(JSONObject jsonEntity) throws JSONException {
	        
//	        bookmarkEntity.setLdesc(jsonEntity.getString("ldesc"));
//	        bookmarkEntity.setSdesc(jsonEntity.getString("sdesc"));
//	        bookmarkEntity.setUpdated(new Date());
//	        
//	        TransactionManager.manage(new Transactional(em) { 
//	             public void transact() {
//	            em.merge(bookmarkEntity);
//	        }});
//	    }    
	  /*
	   @Produces("application/json")
	    public JSONArray getUsersAsJsonArray() {
	        JSONArray uriArray = new JSONArray();
	        for (UserEntity userEntity : getUsers()) {
	            UriBuilder ub = uriInfo.getAbsolutePathBuilder();
	            URI userUri = ub
	                  .path(userEntity.getUserid())
	                  .build();
	            uriArray.put(userUri.toASCIIString());
	        }
	        return uriArray;
	    }	  
	@Path("{bmid: .+}")
    public BookmarkResource getBookmark(@PathParam("bmid") String bmid) {
        return new BookmarkResource(uriInfo, em, 
                userResource.getUserEntity(), bmid);
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JSONArray getBookmarksAsJsonArray() {
        JSONArray uriArray = new JSONArray();
        for (BookmarkEntity bookmarkEntity : getBookmarks()) {
            UriBuilder ub = uriInfo.getAbsolutePathBuilder();
            URI bookmarkUri = ub.
                    path(bookmarkEntity.getBookmarkEntityPK().getBmid()).
                    build();
            uriArray.put(bookmarkUri.toASCIIString());
        }
        return uriArray;
    }
	public JSONObject asJson() {
        try {
            return new JSONObject()
                    .put("userid", bookmarkEntity.getBookmarkEntityPK().getUserid())
                    .put("sdesc", bookmarkEntity.getSdesc())
                    .put("ldesc", bookmarkEntity.getLdesc())
                    .put("uri", bookmarkEntity.getUri());
        } catch (JSONException je){
            return null;
        }
    }
    @DELETE
    public void deleteBookmark() {
        TransactionManager.manage(new Transactional(em) { 
          public void transact() {
            UserEntity userEntity = bookmarkEntity.getUserEntity();
            userEntity.getBookmarkEntityCollection().remove(bookmarkEntity);
            em.merge(userEntity);
            em.remove(bookmarkEntity);
        }});
    }    
   @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postForm(JSONObject bookmark) throws JSONException {
        final BookmarkEntity bookmarkEntity = new BookmarkEntity(getBookmarkId(bookmark.getString("uri")), 
                userResource.getUserEntity().getUserid());
 
        bookmarkEntity.setUri(bookmark.getString("uri"));
        bookmarkEntity.setUpdated(new Date());
        bookmarkEntity.setSdesc(bookmark.getString("sdesc"));
        bookmarkEntity.setLdesc(bookmark.getString("ldesc"));
        userResource.getUserEntity().getBookmarkEntityCollection().add(bookmarkEntity);
 
        TransactionManager.manage(new Transactional(em) { 
           public void transact() {
            em.merge(userResource.getUserEntity());
        }});
 
        URI bookmarkUri = uriInfo.getAbsolutePathBuilder().
                path(bookmarkEntity.getBookmarkEntityPK().getBmid()).
                build();
        return Response.created(bookmarkUri).build();
    }
        */
}
