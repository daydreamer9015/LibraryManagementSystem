package com.library.dto.board;

import com.library.entity.board.BoardFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
    게시글 첨부파일 DTO
        - 게시글 상세 조회시 첨부파일 정보를 클라이언트에 전달
        - BoardFile Entity의 필요한 정보만 선별하여 노출
        - 파일 다운로드 링크 생성에 필요한 엔티티에서 정보 제공

    BoardFile Entity
        - JPA가 관리하는 영속성 객체 (Persistence)
        - Board와의 연관관계 (@ManyToOne)로 포함
        - BaseEntity 상속 (createdAt, updatedAt)
        - 비즈니스 로직 포함

    BoardFileDTO
        - 클라이언트에 전달할 데이터만 포함
        - 연관관계 필요없어서 제외
        - 읽기 전용(@Getter만 사용)
        - 추가 메서드 제공
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFileDTO {
    private Long id;    // 파일 ID(PK) - 다운로드 링크 생성 시 사용
    private String originalFilename;    // 원본 파일명 - 사용자가 업로드한 파일의 원래 이름
    private String storedFilename;      // 저장된 파일명 (UUID) - 서버에 실제로 저장된 파일 이름
    private String filePath;            // 파일 경로(서버의 상대 경로 - "board/2025/10/16")
    private Long fileSize;              // 파일 크기 (바이트 단위)
    private String fileExtension;       // 파일 확장자 (소문자)
    private Long downloadCount;         // 다운로드 횟수
    
    // 사람이 읽기 쉬운 파일 크기 문자열 리턴하는 메서드
    public String getFormattedFileSize() {
        // 1. 1024 미만 (바이트 단위)
        if (fileSize < 1024) {
            return fileSize + "B";
        }
        // 2. 1KB ~ 1MB 미만 (kilobyte 단위)
        else if (fileSize < 1024 * 1024) {
            return String.format("%.1f KB", fileSize / 1024.0);
        }
        // 3. 1MB ~ 1GB 미만 (megabyte 단위)
        else if (fileSize < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
        }
        // 4. 1GB 이상 (gigabyte 단위)
        else {
            return String.format("%.1f GB", fileSize / (1024.0 * 1024.0 * 1024.0));
        }
    }
    /*
        정적 팩토링 메서드 - BoardFile Entity를 BoardFileDTO로 변환
            - static 메서드로 객체를 생성하는 패턴
            - 생성자 대신 의미있는 이름으로 객체 생성
            - Entity -> DTO 변환 로직을 캡슐화
            - 장점
                - 메서드 이름으로 의도 명확히 표현 (from, of, valurOf 등)
                - 생성 로직 중앙화 (변환 규칙 한 곳에서 관리)
                - 필요한 필드만 선택적으로 복사
                - Stream과의 조합
     */
    public static BoardFileDTO from(BoardFile file) {
        return BoardFileDTO.builder()
                .id(file.getId())
                .originalFilename(file.getOriginalFilename())
                .storedFilename(file.getStoredFilename())
                .filePath(file.getFilePath())
                .fileSize(file.getFileSize())
                .fileExtension(file.getFileExtension())
                .downloadCount(file.getDownloadCount())
                .build();
    }
}
