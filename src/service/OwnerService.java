package service;

import model.Owner;
import repository.DataStore;
import repository.OwnerDAO;

public class OwnerService {

    private OwnerDAO ownerDAO = new OwnerDAO();

    public Owner registerOwner(String name,
                               String phoneNumber,
                               String email,
                               String password) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Owner existingOwner = ownerDAO.findByPhone(phoneNumber);

        if (existingOwner != null) {
            throw new IllegalArgumentException(
                    "Phone number already registered"
            );
        }

        Owner owner = new Owner(
                null,
                name,
                phoneNumber,
                email,
                password
        );

        Owner savedOwner = ownerDAO.addOwner(owner);

        if (savedOwner == null) {
            throw new IllegalArgumentException(
                    "Owner registration failed"
            );
        }

        DataStore.owners.put(
                savedOwner.getOwnerId(),
                savedOwner
        );

        return savedOwner;
    }

    public Owner loginOwner(String phoneNumber,
                             String password) {

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Phone number cannot be empty"
            );
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Password cannot be empty"
            );
        }

        Owner owner = ownerDAO.findByPhone(phoneNumber);

        if (owner != null &&
            owner.getPassword().equals(password)) {

            DataStore.owners.put(
                    owner.getOwnerId(),
                    owner
            );

            return owner;
        }

        return null;
    }

    public Owner findOwnerById(Integer ownerId) {

        if (ownerId == null) {
            throw new IllegalArgumentException(
                    "Owner ID cannot be null"
            );
        }

        Owner owner = ownerDAO.findOwnerById(ownerId);

        if (owner != null) {
            DataStore.owners.put(
                    owner.getOwnerId(),
                    owner
            );
        }

        return owner;
    }
}