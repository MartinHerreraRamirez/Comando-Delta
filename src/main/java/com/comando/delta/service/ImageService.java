package com.comando.delta.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.comando.delta.model.Image;
import com.comando.delta.repository.IImageRepository;

@Service
public class ImageService {

    @Autowired
    private IImageRepository imageRepository;

    @Transactional
    public Image saveImage(MultipartFile file) throws Exception{
        if(file != null && !file.isEmpty()){
            try {
                Image image = new Image();

                image.setName(file.getOriginalFilename());
                image.setMime(file.getContentType());
                image.setContent(file.getBytes());

                return imageRepository.save(image);
            } catch (Exception e) {                
            }
        }

        return null;        
    }


    @Transactional
    public Image editImage(MultipartFile file, Long id) throws Exception {
        try {
            Image image;
    
            if (id != null && imageRepository.existsById(id)) {
                image = imageRepository.findById(id).orElse(null);
            } else {
                image = new Image();
            }
    
            if (file != null && !file.isEmpty()) {
                image.setName(file.getOriginalFilename());
                image.setMime(file.getContentType());
                image.setContent(file.getBytes());
            }
    
            return imageRepository.save(image);
        } catch (Exception e) {
            throw new Exception("Error al editar la imagen", e);
        }
    }   

    
}
