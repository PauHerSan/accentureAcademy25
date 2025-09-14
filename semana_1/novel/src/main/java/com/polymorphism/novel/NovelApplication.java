package com.polymorphism.novel;

import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.model.webToon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NovelApplication {

	public static void main(String[] args) {
		SpringApplication.run(NovelApplication.class, args);

        novels novel1 = new novels("Solo Leveling", "Chugong", "D&C Media", 2016, "Acción", "Unkwon", 270 );
        publication.add(novel1);


        webToon webtoon1 = new webToon("Tower of God", "SIU", "Naver Webtoon", 2010, "Acción", "SIU",true);
        publication.add(webtoon1);

	}

}
