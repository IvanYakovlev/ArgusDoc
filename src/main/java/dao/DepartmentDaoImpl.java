package dao;

import model.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DepartmentDaoImpl implements DepartmentDao {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentDaoImpl.class);
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addDepartment(Department department) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(department);
        logger.info("Department succesfully saved. Department details:" +department);
    }

    @Override
    public void removeDepartment(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Department department = (Department) session.load(Department.class,new Integer(id));

        if (department!=null){
            session.delete(department);
        }
        logger.info("Department succesfully remove. Department details:" +department);
    }

    @Override
    public List<Department> listDepartments() {
        Session session = this.sessionFactory.getCurrentSession();
        List<Department> departmentList = session.createQuery("from DEPARTMENTS").list();

        for (Department department: departmentList){
            logger.info("Department list:"+department);
        }
        return departmentList;
    }
}
