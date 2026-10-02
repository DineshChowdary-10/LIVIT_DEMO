package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.Owner;
import model.PG;
import repository.DataStore;
import repository.OwnerDAO;
import repository.PGDAO;

public class PGService {

    private PGDAO pgDAO = new PGDAO();
    private OwnerDAO ownerDAO = new OwnerDAO();

    public PG addPG(PG pg) {

        if (pg == null) {
            throw new IllegalArgumentException("PG cannot be null");
        }

        if (pg.getName() == null || pg.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("PG name cannot be empty");
        }

        if (pg.getLocation() == null || pg.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }

        if (pg.getOwnerId() == null) {
            throw new IllegalArgumentException("Owner ID cannot be null");
        }

        Owner owner = ownerDAO.findOwnerById(pg.getOwnerId());

        if (owner == null) {
            throw new IllegalArgumentException("Owner not found");
        }

        if (pg.getMonthlyRent() == null ||
            pg.getMonthlyRent().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid monthly rent");
        }

        if (pg.getAvailableBeds() == null ||
            pg.getAvailableBeds() < 0) {
            throw new IllegalArgumentException("Invalid available beds");
        }

        if (pg.getTotalBeds() == null ||
            pg.getTotalBeds() <= 0) {
            throw new IllegalArgumentException("Invalid total beds");
        }

        if (pg.getAvailableBeds() > pg.getTotalBeds()) {
            throw new IllegalArgumentException(
                    "Available beds cannot exceed total beds"
            );
        }

        PG savedPG = pgDAO.addPG(pg);

        if (savedPG == null) {
            throw new IllegalArgumentException("PG could not be added");
        }

        DataStore.pgs.add(savedPG);
        DataStore.pgMap.put(savedPG.getPgId(), savedPG);

        return savedPG;
    }

    public List<PG> getAllPGs() {

        List<PG> pgs = pgDAO.getAllPGs();

        DataStore.pgs.clear();
        DataStore.pgMap.clear();

        DataStore.pgs.addAll(pgs);

        for (PG pg : pgs) {
            DataStore.pgMap.put(pg.getPgId(), pg);
        }

        return DataStore.pgs;
    }

    public PG findPGById(Integer pgId) {

        if (pgId == null) {
            throw new IllegalArgumentException("PG ID cannot be null");
        }

        PG pg = pgDAO.findPGById(pgId);

        if (pg != null) {
            DataStore.pgMap.put(pg.getPgId(), pg);

            DataStore.pgs.removeIf(
                    existingPG -> existingPG.getPgId().equals(pg.getPgId())
            );

            DataStore.pgs.add(pg);
        }

        return pg;
    }

    public List<PG> findPGsByOwner(Integer ownerId) {

        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID cannot be null");
        }

        List<PG> pgs = pgDAO.findPGsByOwner(ownerId);

        return pgs;
    }

    public List<PG> searchPGs(String location) {

        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }

        List<PG> allPGs = getAllPGs();
        List<PG> results = new ArrayList<>();

        for (PG pg : allPGs) {

            if (pg.getLocation() != null &&
                pg.getLocation()
                  .toLowerCase()
                  .contains(location.trim().toLowerCase())) {

                results.add(pg);
            }
        }

        return results;
    }

    public List<PG> filterPGs(BigDecimal maxRent,
                              String sharingType,
                              Boolean availableOnly) {

        List<PG> allPGs = getAllPGs();
        List<PG> results = new ArrayList<>();

        for (PG pg : allPGs) {

            boolean matches = true;

            if (maxRent != null &&
                pg.getMonthlyRent().compareTo(maxRent) > 0) {

                matches = false;
            }

            if (sharingType != null &&
                !sharingType.trim().isEmpty() &&
                !sharingType.equalsIgnoreCase("Any") &&
                !pg.getSharingType().equalsIgnoreCase(sharingType)) {

                matches = false;
            }

            if (Boolean.TRUE.equals(availableOnly) &&
                pg.getAvailableBeds() <= 0) {

                matches = false;
            }

            if (matches) {
                results.add(pg);
            }
        }

        return results;
    }

    public List<PG> sortPGs(List<PG> pgList, String sortBy) {

        if (pgList == null) {
            throw new IllegalArgumentException("PG list cannot be null");
        }

        if (sortBy == null || sortBy.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Sort option cannot be empty"
            );
        }

        switch (sortBy.toLowerCase()) {

            case "rent":
                pgList.sort(
                        Comparator.comparing(PG::getMonthlyRent)
                );
                break;

            case "rating":
                pgList.sort(
                        Comparator.comparing(PG::getRating).reversed()
                );
                break;

            case "distance":
                pgList.sort(
                        Comparator.comparing(PG::getDistance)
                );
                break;

            case "vacancy":
                pgList.sort(
                        Comparator.comparing(PG::getAvailableBeds).reversed()
                );
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid sort option"
                );
        }

        return pgList;
    }
}