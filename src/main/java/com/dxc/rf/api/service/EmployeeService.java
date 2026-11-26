package com.dxc.rf.api.service;

import com.dxc.rf.api.exception.DuplicateEmailException;
import com.dxc.rf.api.exception.EmployeeNotFoundException;
import com.dxc.rf.api.model.Employee;
import com.dxc.rf.api.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee create(Employee employee) {
        if (repository.existsByEmailIgnoreCase(employee.getEmail())) {
            throw new DuplicateEmailException(employee.getEmail());
        }
        return repository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee update(Long id, Employee changes) {
        Employee existing = findById(id);
        existing.setFirstName(changes.getFirstName());
        existing.setLastName(changes.getLastName());
        existing.setEmail(changes.getEmail());
        existing.setDepartment(changes.getDepartment());
        existing.setSalary(changes.getSalary());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
