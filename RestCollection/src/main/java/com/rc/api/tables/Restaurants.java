package com.rc.api.tables;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.rc.api.SessionUtil;

@Entity
@Table(name = "restaurants")
public class Restaurants {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String city;
	private Integer estimatedCost;
	private Float averageRating;
	private Integer votes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(Integer estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public Float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Float averageRating) {
		this.averageRating = averageRating;
	}

	public Integer getVotes() {
		return votes;
	}

	public void setVotes(Integer votes) {
		this.votes = votes;
	}

	public Integer add() throws Exception {
		Transaction tx = null;
		Integer rowId = null;
		try (Session session = SessionUtil.getSession()) {
			tx = session.beginTransaction();
			rowId = (Integer) session.save(this);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (RuntimeException rbe) {
				}
			}
			throw new Exception("Error: " + e.getCause().getMessage());
		}
		return rowId;
	}

	public static Restaurants getRestaurant(Integer id) throws Exception {
		Restaurants restaurant = null;
		try (Session session = SessionUtil.getSession()) {
			restaurant = session.get(Restaurants.class, id);

		} catch (HibernateException e) {
			e.printStackTrace();
			String err = e.getCause().toString();
			throw new Exception("Error: " + err);
		}
		return restaurant;
	}

	public void updateRestaurant() throws Exception {
		try (Session session = SessionUtil.getSession()) {
			Transaction tr = session.beginTransaction();
			session.update(this);
			tr.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			String err = e.getCause().toString();
			throw new Exception("Error: " + err);
		}
	}

	public void deleteRestaurant() throws Exception {
		try (Session session = SessionUtil.getSession()) {
			Transaction tr = session.beginTransaction();
			session.delete(this);
			tr.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			String err = e.getCause().toString();
			throw new Exception("Error: " + err);
		}
	}

	public static List<Restaurants> getAllRestaurants(boolean isSortedRequired) throws Exception {
		List<Restaurants> restaurantList = new ArrayList<Restaurants>();
		try (Session session = SessionUtil.getSession()) {
			String query;
			if (isSortedRequired) {
				query = String.format("FROM Restaurants order by averageRating desc");
			} else {
				query = String.format("FROM Restaurants");
			}

			for (Object oneObject : session.createQuery(query).getResultList()) {
				restaurantList.add((Restaurants) oneObject);
			}
		} catch (HibernateException e) {
			String err = e.getCause().toString();
			throw new Exception("Error: " + err);
		}
		return restaurantList;
	}

	public static List<Restaurants> getRestaurants(String city) throws Exception {
		List<Restaurants> restaurantList = new ArrayList<Restaurants>();
		try (Session session = SessionUtil.getSession()) {

			String query = String.format("FROM Restaurants where city = '%s'", city);
			for (Object oneObject : session.createQuery(query).getResultList()) {
				restaurantList.add((Restaurants) oneObject);
			}
		} catch (HibernateException e) {
			String err = e.getCause().toString();
			throw new Exception("Error: " + err);
		}
		return restaurantList;
	}

}
