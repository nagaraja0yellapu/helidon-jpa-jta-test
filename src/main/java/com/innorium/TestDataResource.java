package com.innorium;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author nagaraj
 */
@Path("/testData")
@RequestScoped
public class TestDataResource {
    private final Logger LOGGER = Logger.getLogger(TestDataResource.class.getName());

    private static final int MAX_FETCH_SIZE = 250;

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = BadRequestException.class)
    public Response test(@QueryParam("id") Long id) {
        try {
            LOGGER.info("test::" + id);

            TestData testData = entityManager.find(TestData.class, id);
            if (testData == null) {
                throw new BadRequestException("no data found to test, create data before testing..");
            }

            testData.setDescription("data should not save as the exception is thrown, it seems to be bug!!!");

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<TestData> cq = cb.createQuery(TestData.class);
            Root<TestData> root = cq.from(TestData.class);

            cq.select(root).where(cb.like(root.get("name"), "%test%"));
            TypedQuery<TestData> q = entityManager.createQuery(cq);
            q.getSingleResult();

            boolean throwException = true;
            if (throwException) {
                throw new BadRequestException("Testing data save with rollbackOn=BadRequestException");
            }
            return Response.status(Response.Status.OK).entity(testData).build();
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @GET
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = BadRequestException.class)
    public Response create(@QueryParam("name") String name) {
        try {
            LOGGER.info("save::" + name);

            TestData testData = new TestData();
            testData.setName(name);
            testData.setCreatedBy("admin");
            testData.setCreatedTime(new Date());
            testData.setLastModifiedTime(new Date());

            entityManager.persist(testData);
            return Response.status(Response.Status.OK).entity(testData).build();
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @GET
    @Path("getById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public Response get(@PathParam("id") Long id) {
        try {
            if (id == null) {
                return Response.status(Response.Status.OK).entity("Id cann't be empty").build();
            }

            TestData model = entityManager.find(TestData.class, id);

            if (model == null) {
                return Response.status(Response.Status.OK).entity("TestData does not exist").build();
            }

            return Response.status(Response.Status.OK).entity(model).build();
        } catch (Exception e) {
            return Response.status(Response.Status.OK).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }
}
