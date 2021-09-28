package com.urlshortener.service;

import org.springframework.stereotype.Service;

import com.urlshortener.model.Url;
import com.urlshortener.model.UrlDto;


public interface UrlService {

	public Url generateShortLink(UrlDto urlDto);
	public Url persistShortLink(Url url);
	public Url getEncoderUrl(String url);
	public void deleteShortLink(Url url);
}
