package com.exoplatform;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.organization.*;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/*
* REST Service for user managment .
*
* @author fares maatoug
*/
@Path("/usermanagment")
@Produces("application/json")
public class RestUserService implements ResourceContainer {

    private List<String> types = new ArrayList<String>();
    private List<String> groups = new ArrayList<String>();

    @GET
    @Path("/addgroups/{nbgroups}/{nbmemeberships}/{username}")
    public void createGroup(String parentId, @PathParam("nbgroups") int nbgroups,@PathParam("nbmemeberships") int nbmemberships, @PathParam("username") String username) throws Exception {

        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        OrganizationService organizationService = (OrganizationService) ExoContainerContext.getCurrentContainer()
                .getComponentInstanceOfType(OrganizationService.class);
        GroupHandler gHandler = organizationService.getGroupHandler();
        UserHandler uHandler = organizationService.getUserHandler();
        MembershipHandler mHandler = organizationService.getMembershipHandler();
        MembershipTypeHandler mtHandler = organizationService.getMembershipTypeHandler();

        for(int i =0;i<= nbgroups ;i++) {

            Group parent = parentId == null ? null : gHandler.findGroupById(parentId);
            SecureRandom random = new SecureRandom();
            String co = new BigInteger(130, random).toString(32);

            Group child = gHandler.createGroupInstance();
            child.setGroupName("gg" + co);
            child.setLabel("ggll" + co );
            child.setDescription("ggdd" + co);
            gHandler.addChild(parent, child, true);



            groups.add((parent == null ? "" : parentId) + "/" + child.getGroupName());
            for(int j =0;j<= nbmemberships ;j++) {
                String ss = "" + j ;

                MembershipType mt = mtHandler.createMembershipTypeInstance();
                String membershipType = ("mmtt" + co + ss );
                mt.setName(membershipType);
                mt.setDescription("mmdd" + co + ss);
                mtHandler.createMembershipType(mt, true);
                types.add(membershipType);
                mHandler.linkMembership(uHandler.findUserByName(username), gHandler.findGroupById("/" + child.getGroupName()), mtHandler
                        .findMembershipType(membershipType), true);
            }
        }
    }

}



