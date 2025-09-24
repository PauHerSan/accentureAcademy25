package com.javatechie;

import com.javatechie.tools.ShoppingCartMcpService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ShoppingCartMcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartMcpApplication.class, args);
	}

	@Bean
	public List<ToolCallback> shoppingCartToolCallback(ShoppingCartMcpService cartMcpService){
		return List.of(ToolCallbacks.from(cartMcpService));
	}

}
