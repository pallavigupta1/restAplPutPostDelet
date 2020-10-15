package com.rc.api.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.rc.api.rest.output.ResponseBean;
import com.rc.api.tables.Restaurants;

@Path("restaurant")
public class RestaurantEndPoints {

	Gson gson = new Gson();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurant(Restaurants r) {
		return processAddRestaurant(r);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRestaurants() {
		return processGetRestaurants();
	}

	@Path("query")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRestaurants(@QueryParam("city") String city, @QueryParam("id") Integer id) {
		return processGetRestaurants(city, id);
	}

	@Path("sort")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRestaurantsSorted() {
		return processGetRestaurantsSorted();
	}

	@Path("{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRestaurantById(@PathParam("id") Integer id) {
		return processDeleteRestaurants(id);

	}
	
	@Path("{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRestaurantById(@PathParam("id") Integer id, Restaurants r) {
		return processUpdateRestaurants(id, r);

	}
	
	
	private Response processUpdateRestaurants(Integer id, Restaurants r) {
		Response resBean = null;
		ResponseBean rb = new ResponseBean();
		rb.status = 0;
		rb.message = "Failed";

		try {

			Restaurants rr = Restaurants.getRestaurant(id);

			if (rr != null) {
				rr.setAverageRating(r.getAverageRating());
				rr.setVotes(r.getVotes());
				rr.updateRestaurant();
			}
			else {
				throw new Exception("No Restaurant found for Id: " + id);
			}
			rb.status = 1;
			rb.message = "Success";
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			ex.getStackTrace();
			rb.error = ex.getLocalizedMessage();
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).type(MediaType.APPLICATION_JSON)
					.build();
		}
		return resBean;
	}

	private Response processDeleteRestaurants(Integer id) {
		Response resBean = null;
		ResponseBean rb = new ResponseBean();
		rb.status = 0;
		rb.message = "Failed";

		try {

			Restaurants rr = Restaurants.getRestaurant(id);

			if (rr != null) {
				rr.deleteRestaurant();
			}
			else {
				throw new Exception("No Restaurant found for Id: " + id);
			}
			rb.status = 1;
			rb.message = "Success";
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			ex.getStackTrace();
			rb.error = ex.getLocalizedMessage();
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).type(MediaType.APPLICATION_JSON)
					.build();
		}
		return resBean;
	}

	private Response processGetRestaurantsSorted() {
		Response resBean = null;
		ResponseBean rb = new ResponseBean();
		rb.status = 0;
		rb.message = "Failed";

		try {
			List<Restaurants> ll = Restaurants.getAllRestaurants(true);
			if (ll != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("restaurants", ll);
				rb.data = map;
				rb.status = 1;
				rb.message = "Success";
			}
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			ex.getStackTrace();
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).type(MediaType.APPLICATION_JSON)
					.build();
		}
		return resBean;
	}

	private Response processGetRestaurants(String city, Integer id) {
		Response resBean = null;
		ResponseBean rb = new ResponseBean();
		rb.status = 0;
		rb.message = "Failed";

		try {

			if (city == null && id == null) {
				throw new IllegalArgumentException("No query param was passed");
			}
			List<Restaurants> ll;
			Map<String, Object> map = new HashMap<String, Object>();
			Restaurants rr;
			if (city != null) {
				ll = Restaurants.getRestaurants(city);
				if (ll != null) {
					map.put("restaurants", ll);
				}
			} else if (id != null) {
				rr = Restaurants.getRestaurant(id);
				if (rr != null) {
					map.put("restaurant", rr);
				}
			}
			rb.data = map;
			rb.status = 1;
			rb.message = "Success";
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			ex.getStackTrace();
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).type(MediaType.APPLICATION_JSON)
					.build();
		}
		return resBean;
	}

	private Response processGetRestaurants() {
		Response resBean = null;
		ResponseBean rb = new ResponseBean();
		rb.status = 0;
		rb.message = "Failed";

		try {
			List<Restaurants> ll = Restaurants.getAllRestaurants(false);
			if (ll != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("restaurants", ll);
				rb.data = map;
				rb.status = 1;
				rb.message = "Success";
			}
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			ex.getStackTrace();
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).type(MediaType.APPLICATION_JSON)
					.build();
		}
		return resBean;
	}

	private Response processAddRestaurant(Restaurants r) {
		Response resBean = null;
		ResponseBean rb = new ResponseBean();
		rb.status = 0;
		rb.message = "Failed";

		try {
			Integer rowId = r.add();
			if (rowId != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", rowId);
				rb.data = map;
				rb.status = 1;
				rb.message = "Success";
			}
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			ex.getStackTrace();
			String res = gson.toJson(rb, ResponseBean.class);
			resBean = Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).type(MediaType.APPLICATION_JSON)
					.build();
		}
		return resBean;
	}

}
