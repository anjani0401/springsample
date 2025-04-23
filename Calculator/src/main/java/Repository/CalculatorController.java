package Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calculate/")
public class CalculatorController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("add")
    public ResponseEntity<?> addNumbers(@RequestParam String numbersList){

        if("".equalsIgnoreCase(numbersList.trim()) && !numbersList.matches("-?\\d+(\\.\\d+)?")){
            return new ResponseEntity<>("Input does not contain valid values", HttpStatusCode.valueOf(400));
        }

        List<Integer> numbers = Arrays.stream(numbersList.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();

        int sum = numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();

        redisTemplate.opsForValue().set("SUM", sum+"");

        return new ResponseEntity<>(sum, HttpStatusCode.valueOf(200));
    }
}
