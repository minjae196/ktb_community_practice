package com.example.demo.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class ImageProcessor {

    public static class ProcessedFiles {
        private File jpgFile;
        private File webpFile;

        public File getJpgFile(){
            return jpgFile;
        }

        public File getWebpFile(){
            return webpFile;
        }

        public ProcessedFiles(File jpg, File webp){
            this.jpgFile = jpg;
            this.webpFile = webp;
        }
    }

    public ProcessedFiles processImage(MultipartFile file, String type){
        try{
            BufferedImage inputImage = ImageIO.read(file.getInputStream());

            float quality = getCompressionQuality(type);

            File jpgFile = compressToJPG(inputImage, quality);

            File webpFile = convertToWebp(inputImage,quality);

            return new ProcessedFiles(jpgFile,webpFile);
        } catch (IOException e){
            throw new RuntimeException("이미지 변환 실패", e);
        }
    }

    // 이 밑부분은 AI의 도움을 받았음.

    // 1. 이미지 용도(타입)에 따라 압축률을 결정하는 메서드
    private float getCompressionQuality(String type) {
        if ("PROFILE".equalsIgnoreCase(type)) {
            return 0.8f; // 프로필 사진은 화질을 조금 더 높게 유지 (80%)
        } else if ("POST".equalsIgnoreCase(type)) {
            return 0.6f; // 게시글 사진은 용량 최적화를 위해 압축률을 높임 (60%)
        }
        return 0.7f; // 기본값
    }

    // 2. 이미지를 JPG 포맷으로 압축하여 임시 파일로 저장하는 메서드
    private File compressToJPG(BufferedImage image, float quality) throws IOException {
        // 임시 파일 생성
        File tempFile = File.createTempFile("compressed_", ".jpg");

        // (꿀팁) PNG처럼 투명 배경이 있는 이미지를 JPG로 바꿀 때 배경이 까맣게 깨지는 것을 방지하기 위해 흰색 배경을 칠해줍니다.
        BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g = rgbImage.createGraphics();
        g.drawImage(image, 0, 0, java.awt.Color.WHITE, null);
        g.dispose();

        // 압축 설정 및 파일 쓰기
        java.util.Iterator<javax.imageio.ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) throw new IllegalStateException("JPG Writer를 찾을 수 없습니다.");
        javax.imageio.ImageWriter writer = writers.next();

        try (javax.imageio.stream.ImageOutputStream ios = ImageIO.createImageOutputStream(tempFile)) {
            writer.setOutput(ios);
            javax.imageio.ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
            }
            writer.write(null, new javax.imageio.IIOImage(rgbImage, null, null), param);
        } finally {
            writer.dispose();
        }

        return tempFile;
    }

    // 3. 이미지를 WebP 포맷으로 변환하여 임시 파일로 저장하는 메서드
    private File convertToWebp(BufferedImage image, float quality) throws IOException {
        File tempFile = File.createTempFile("compressed_", ".webp");

        // WebP 변환용 Writer 찾기
        java.util.Iterator<javax.imageio.ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/webp");

        // ✨ 아주 중요한 부분: WebP는 자바 기본 기능이 아닙니다!
        if (!writers.hasNext()) {
            // build.gradle에 WebP 라이브러리(예: org.sejda.imageio:webp-imageio)가 없다면 에러가 납니다.
            // 일단 테스트를 위해 라이브러리가 없으면 임시로 JPG로 우회해서 저장하도록 방어 코드를 짜두었습니다.
            return compressToJPG(image, quality);
        }

        javax.imageio.ImageWriter writer = writers.next();
        try (javax.imageio.stream.ImageOutputStream ios = ImageIO.createImageOutputStream(tempFile)) {
            writer.setOutput(ios);
            javax.imageio.ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
            }
            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }

        return tempFile;
    }
}
