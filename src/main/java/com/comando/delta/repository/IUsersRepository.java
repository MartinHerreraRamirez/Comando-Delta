package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.Hierarchy;
import com.comando.delta.model.Users;

@Repository
public interface IUsersRepository extends JpaRepository<Users, Long>{
    @Query("SELECT u FROM Users u WHERE u.rol != 'SUPERADMIN' ORDER BY u.id DESC")
    List<Users> findUsersList();

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    public Users findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM Users u WHERE u.lastName = :lastName")
    public List<Users> findUserByLastName(@Param("lastName") String lastName);

    @Query("SELECT u FROM Users u WHERE u.personalFile = :personalFile")
    public Users findUserByPersonalFile(@Param("personalFile") String personaFile);

    @Query("SELECT u FROM Users u WHERE u.operatorNumber = :operatorNumber")
    public Users findUserByOperatorNumber(@Param("operatorNumber") String operatorNumber);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.email = :email")
    public Boolean existenceUserByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.operatorNumber = :operatorNumber")
    public Boolean existenceUserByOperatorNumber(@Param("operatorNumber") String operatorNumber);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.phoneNumber = :phoneNumber")
    public Boolean existenceUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.email = :email AND (:id IS NULL OR u.id != :id)")
    public Boolean existenceUpdateUserByEmail(@Param("email") String email, @Param("id") Long id);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.operatorNumber = :operatorNumber AND (:id IS NULL OR u.id != :id)")
    public Boolean existenceUpdateUserByOperatorNumber(@Param("operatorNumber") String operatorNumber, @Param("id") Long id);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.phoneNumber = :phoneNumber AND (:id IS NULL OR u.id != :id)")
    public Boolean existenceUpdateUserByPhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("id") Long id);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.hierarchy = :hierarchy AND u.personalFile = :personalFile")
    public Boolean existenceUserByHierarchyAndPersonalFile(@Param("hierarchy") Hierarchy hierarchy, @Param("personalFile") String personalFile);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.hierarchy = :hierarchy AND u.personalFile = :personalFile AND (:id IS NULL OR u.id != :id)")
    public Boolean existenceUpdateUserByHierarchyAndPersonalFile(@Param("hierarchy") Hierarchy hierarchy, @Param("personalFile") String personalFile, @Param("id") Long id);


}
