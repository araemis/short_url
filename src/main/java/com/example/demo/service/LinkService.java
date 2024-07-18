package com.example.demo.service;

import com.example.demo.model.Link;
import com.example.demo.password.PassEncTech1;
import com.example.demo.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;

    public Link save(Link link) {

        String shortLink;
        do {
            shortLink = RandomStringUtils.randomAlphanumeric(5, 10);
        } while (linkRepository.getLinkByUrl(shortLink)
                               .isPresent());


        link.setUrl(shortLink);

        link.setBigUrl(checkUrlHttp(link.getBigUrl()));

        if (!link.getPassword()
                 .trim()
                 .isEmpty()) {
            String encryptedPassword = PassEncTech1.encrypt(link.getPassword());
            link.setPassword(encryptedPassword);
        }

        return linkRepository.save(link);

    }

    public Link getLink(String shortLink) {
        return linkRepository.getLinkByUrl(shortLink)
                             .orElseThrow(() -> new ErrorResponseException(HttpStatus.NOT_FOUND));
    }

    @Scheduled(fixedRate = 60000)
    public void deleteExpiredRows() {
        LocalDate now = LocalDate.now();
        linkRepository.deleteByTimeDeleteBefore(now);
    }


    private String checkUrlHttp(String url) {
        if (url.startsWith("https://") || url.startsWith("http://")) {
            return url;
        }
        url = "https://" + url;

        return url;
    }

}
