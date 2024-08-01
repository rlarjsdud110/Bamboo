package com.example.bamboo.service;

import com.example.bamboo.dto.request.board.CreateRequestBoard;
import com.example.bamboo.dto.request.board.UpdateRequestBoard;
import com.example.bamboo.dto.response.board.GetBoardDTO;
import com.example.bamboo.dto.response.board.GetBoardListDTO;
import com.example.bamboo.dto.response.board.GetSearchBoardList;
import com.example.bamboo.dto.response.board.GetUserBoardListDTO;
import com.example.bamboo.entity.BoardEntity;
import com.example.bamboo.entity.BoardImageEntity;
import com.example.bamboo.entity.UserEntity;
import com.example.bamboo.exception.CustomException;
import com.example.bamboo.exception.ErrorCode;
import com.example.bamboo.repository.BoardImageRepository;
import com.example.bamboo.repository.BoardRepository;
import com.example.bamboo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createBoard(String email, CreateRequestBoard board) {
        try {
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                log.error("사용자를 찾을 수 없습니다. 이메일: {}", email);
                throw new CustomException("사용자를 찾을 수 없습니다",ErrorCode.NOT_USER_INFO);
            }

            BoardEntity boardEntity = BoardEntity.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .views(0)
                    .favorite(0)
                    .createdAt(LocalDateTime.now())
                    .user(user)
                    .build();

            boardEntity = boardRepository.save(boardEntity);
            log.info("게시판이 성공적으로 생성되었습니다. ID: {}", boardEntity.getBoardId());

            if (board.getBoardImages() == null || board.getBoardImages().isEmpty()) {
                return;
            }

            List<String> boardImageList = board.getBoardImages();
            List<BoardImageEntity> boardImages = new ArrayList<>();
            for (String image : boardImageList) {
                BoardImageEntity imageEntity = new BoardImageEntity(boardEntity, image);
                boardImages.add(imageEntity);
            }

            boardImageRepository.saveAll(boardImages);
            log.info("이미지가 성공적으로 저장되었습니다.");

        } catch (Exception e) {
            log.error("게시판 생성 중 오류가 발생했습니다: {}", e.getMessage());
            throw new CustomException("게시판 생성 중 오류가 발생했습니다",ErrorCode.RETRY);
        }
    }

    public List<GetBoardListDTO> getBoardList(){
        List<BoardEntity> board = boardRepository.findByOrderByCreatedAtDesc();
        List<GetBoardListDTO> boardList = new ArrayList<>();

        for (BoardEntity entity : board) {
            GetBoardListDTO boardListDTO = convertToDTO(entity);
            boardList.add(boardListDTO);
        }

        return boardList;
    }

    public List<GetBoardListDTO> getTopThreeBoard(){
        List<BoardEntity> board = boardRepository.findTop3ByOrderByViewsDesc();
        List<GetBoardListDTO> boardList = new ArrayList<>();

        for (BoardEntity entity : board) {
            GetBoardListDTO boardListDTO = convertToDTO(entity);
            boardList.add(boardListDTO);
        }

        return boardList;
    }

    public GetBoardDTO getBoard(int boardNumber){
        BoardEntity board = boardRepository.findById(boardNumber)
                .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.",ErrorCode.POST_NOT_FOUND));

        UserEntity user = board.getUser();
        if(user == null){
            throw new CustomException("사용자를 찾을 수 없습니다.",ErrorCode.NOT_USER_INFO);
        }

        List<BoardImageEntity> boardImage = boardImageRepository.findByBoard_BoardId(board.getBoardId());
        List<String> imageUrl = new ArrayList<>();

        if (boardImage != null && !boardImage.isEmpty()) {
            for (BoardImageEntity image : boardImage) {
                imageUrl.add(image.getImageUrl());
            }
        }

        return GetBoardDTO.builder()
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .nickname(user.getNickname())
                .boardNumber(board.getBoardId())
                .content(board.getContent())
                .title(board.getTitle())
                .createdAt(board.getCreatedAt())
                .boardImageList(imageUrl)
                .build();
    }

    @Transactional
    public void updateBoard(int boardNumber, UpdateRequestBoard requestBoard) {
        BoardEntity board = boardRepository.findById(boardNumber)
                .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.",ErrorCode.POST_NOT_FOUND));

        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        boardRepository.save(board);

        List<String> newImages = requestBoard.getBoardImages();
        List<BoardImageEntity> existingImages = boardImageRepository.findByBoard_BoardId(boardNumber);

        if (newImages == null || newImages.isEmpty()) {
            boardImageRepository.deleteAll(existingImages);
        } else {
            List<String> existingImageUrls = existingImages.stream().map(BoardImageEntity::getImageUrl).toList();

            for (String img : newImages) {
                if (!existingImageUrls.contains(img)) {
                    BoardImageEntity newImage = new BoardImageEntity();
                    newImage.setBoard(board);
                    newImage.setImageUrl(img);
                    boardImageRepository.save( newImage);
                }
            }

            for (BoardImageEntity image : existingImages) {
                if (!newImages.contains(image.getImageUrl())) {
                    boardImageRepository.delete(image);
                }
            }
        }
    }

    public void deleteBoard(String email, int boardNumber){
        BoardEntity board = boardRepository.findById(boardNumber)
                .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.",ErrorCode.POST_NOT_FOUND));

        UserEntity user = userRepository.findByEmail(email);
        UserEntity currentUser = board.getUser();

        if(user != currentUser){
            throw new CustomException("사용자를 찾을 수 없습니다.",ErrorCode.NOT_USER_INFO);
        }
        boardRepository.delete(board);
    }

    public void viewCount(int boardNumber){
        try {
            BoardEntity board = boardRepository.findById(boardNumber)
                    .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.",ErrorCode.POST_NOT_FOUND));
            board.setViews(board.getViews()+1);
            boardRepository.save(board);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<GetSearchBoardList> searchBoardList(String keyword){
        List<BoardEntity> board = boardRepository.findByTitleContaining(keyword);
        List<GetSearchBoardList> searchBoardList = new ArrayList<>();

        for (BoardEntity entity : board) {
            GetSearchBoardList boardListDTO = convertToSearchDTO(entity);
            searchBoardList.add(boardListDTO);
        }
        return searchBoardList;
    }

    public List<GetUserBoardListDTO> getUserBoardList(String email){
        UserEntity user = userRepository.findByEmail(email);
        List<GetUserBoardListDTO> userBoardListDTO = new ArrayList<>();
        for(BoardEntity board : user.getBoards()){
            String boardImage = board.getBoardImages().stream()
                    .findFirst()
                    .map(BoardImageEntity::getImageUrl)
                    .orElse("");

            GetUserBoardListDTO dto = GetUserBoardListDTO.builder()
                    .boardNumber(board.getBoardId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .boardImage(boardImage)
                    .createdAt(board.getCreatedAt())
                    .nickname(user.getNickname())
                    .profileImg(user.getProfileImg())
                    .views(board.getViews())
                    .favorite(board.getFavorite())
                    .commentCount(board.getComments().size()).build();
            userBoardListDTO.add(dto);
        }
        return userBoardListDTO;
    }

    private GetBoardListDTO convertToDTO(BoardEntity boardEntity) {
        String boardImage = boardEntity.getBoardImages().stream()
                .findFirst()
                .map(BoardImageEntity::getImageUrl)
                .orElse("");

        return GetBoardListDTO.builder()
                .boardNumber(boardEntity.getBoardId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .email(boardEntity.getUser().getEmail())
                .boardImage(boardImage)
                .createdAt(boardEntity.getCreatedAt())
                .nickname(boardEntity.getUser().getNickname())
                .profileImg(boardEntity.getUser().getProfileImg())
                .views(boardEntity.getViews())
                .favorite(boardEntity.getFavorite())
                .commentCount(boardEntity.getComments().size())
                .build();
    }
    private GetSearchBoardList convertToSearchDTO(BoardEntity boardEntity) {
        String boardImage = boardEntity.getBoardImages().stream()
                .findFirst()
                .map(BoardImageEntity::getImageUrl)
                .orElse("");

        return GetSearchBoardList.builder()
                .boardNumber(boardEntity.getBoardId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .email(boardEntity.getUser().getEmail())
                .boardImage(boardImage)
                .createdAt(boardEntity.getCreatedAt())
                .nickname(boardEntity.getUser().getNickname())
                .profileImg(boardEntity.getUser().getProfileImg())
                .views(boardEntity.getViews())
                .favorite(boardEntity.getFavorite())
                .commentCount(boardEntity.getComments().size())
                .build();
    }

}
