package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

import exercise.repository.CarRepository;
import exercise.dto.CarDTO;
import exercise.dto.CarCreateDTO;
import exercise.dto.CarUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.CarMapper;

@RestController
@RequestMapping("/cars")
public class CarsController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarMapper carMapper;

    @GetMapping(path = "")
    public List<CarDTO> index() {
        var cars = carRepository.findAll();
        return cars.stream()
                .map(p -> carMapper.map(p))
                .toList();
    }

    @GetMapping(path = "/{id}")
    public CarDTO show(@PathVariable long id) {

        var car =  carRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Car with id " + id + " not found"));
        var carDTO = carMapper.map(car);
        return carDTO;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO create(@RequestBody CarCreateDTO carData) {
        var car = carMapper.map(carData);
        carRepository.save(car);
        var carDto = carMapper.map(car);
        return carDto;
    }

    @PutMapping(path = "/{id}")
    public CarDTO update(@PathVariable long id, @RequestBody CarUpdateDTO carData) {

        var car =  carRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Car with id " + id + " not found"));

        carMapper.update(carData, car);
        carRepository.save(car);

        return carMapper.map(car);
    }
}
