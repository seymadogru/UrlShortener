package com.urlshortener.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;
import com.urlshortener.model.Url;
import com.urlshortener.model.UrlDto;
import com.urlshortener.repository.UrlRepository;

@Service
public class UrlServiceImpl implements UrlService{

	@Autowired
	private UrlRepository urlRepository;
	
	@Override
	public Url generateShortLink(UrlDto urlDto) {
		
		if(StringUtils.isNotEmpty(urlDto.getUrl())) {
			String encodedUrl = encodeUrl(urlDto.getUrl());
			Url urlToPersist = new Url();
			urlToPersist.setCreationDate(LocalDateTime.now());
			urlToPersist.setOriginalUrl(urlDto.getUrl());
			urlToPersist.setShortLink(encodedUrl);
			urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(), urlToPersist.getCreationDate()));
			Url urlToRet = persistShortLink(urlToPersist);
			
			if(urlToRet != null) {
				return urlToRet;
			}
			return null;
		}
		
		return null;
	}

	
	
	private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
		if(StringUtils.isBlank(expirationDate)) {
			return creationDate.plusSeconds(60);
		}
		LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
		return expirationDateToRet;
	}



	@SuppressWarnings("deprecation")
	private String encodeUrl(String url) {
		String encodeUrl = "";
		LocalDateTime time = LocalDateTime.now();
		encodeUrl = Hashing.murmur3_32().hashString(url.concat(time.toString()), StandardCharsets.UTF_16).toString();
		return encodeUrl;
	}

	@Override
	public Url persistShortLink(Url url) {
		Url urlToRet = urlRepository.save(url);
		return urlToRet;
	}

	@Override
	public Url getEncoderUrl(String url) {
		Url urlToRet = urlRepository.findByShortLink(url);
		return urlToRet;
	}

	@Override
	public void deleteShortLink(Url url) {
		// TODO Auto-generated method stub
		urlRepository.delete(url);
	}

}
