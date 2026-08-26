package kr.co.koscom.minibank.service;

import kr.co.koscom.minibank.domain.Customer;
import kr.co.koscom.minibank.dto.CustomerCreateDto;
import kr.co.koscom.minibank.dto.CustomerResponseDto;
import kr.co.koscom.minibank.dto.CustomerUpdateDto;
import kr.co.koscom.minibank.exception.NotFoundException;
import kr.co.koscom.minibank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerResponseDto create(CustomerCreateDto dto) {
        Customer customer = Customer.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .build();

        Customer saved = customerRepository.save(customer);
        return CustomerResponseDto.from(saved);
    }

    public CustomerResponseDto getById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(NotFoundException::new);
        return CustomerResponseDto.from(customer);
    }

    public List<CustomerResponseDto> getAll() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(CustomerResponseDto::from).toList();
    }

    public void delete(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(NotFoundException::new);
        customerRepository.delete(customer);
        return;
    }

    @Transactional
    public CustomerResponseDto update(Long id, CustomerUpdateDto dto) {
        Customer customer = customerRepository.findById(id).orElseThrow(NotFoundException::new);
        customer.updateInfo(dto.getName(), dto.getPhone());
        return CustomerResponseDto.from(customer);
    }
}
