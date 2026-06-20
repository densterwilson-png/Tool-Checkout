package com.gbmanufacturing.ecs.model;
import com.gbmanufacturing.ecs.dao.EquipmentDAO;

public class Employee extends User {

    public Employee() {
        super();
        setRole(Role.EMPLOYEE);
    }

    public Employee(int userId,
                String username,
                String password,
                String firstName,
                String lastName) {

    super();

    setUserId(userId);
    setUsername(username);
    setPassword(password);
    setFirstName(firstName);
    setLastName(lastName);
    setRole(Role.EMPLOYEE);
}

    public boolean checkoutEquipment(int id) {
        try {
            EquipmentDAO dao = new EquipmentDAO();
            return dao.checkout(id, getUsername(), getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean checkinEquipment(int id) {
        
        try {
            EquipmentDAO dao = new EquipmentDAO();
            return dao.checkin(id, getUsername(), getUserId());
        
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
    
