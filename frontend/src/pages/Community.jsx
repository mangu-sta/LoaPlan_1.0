import { useState, useEffect } from "react";
import "../styles/board.css";
import BoardDetailModal from "../components/BoardDetailModal";
import commentIcon from "../assets/comment.svg";
import viewIcon from "../assets/view.svg";


import defaultProfile from "../assets/default-profile.png"; // ← 기본 이미지 추가됨

const categories = ["자유게시판", "건의사항", "공략대모집", "길드모집", "깐부모집"];

const categoryMap = {
  자유게시판: "FREE",
  건의사항: "SUGGEST",
  공략대모집: "PARTY",
  길드모집: "GUILD",
  깐부모집: "FRIEND",
};

const categoryToKorean = {
  FREE: "자유게시판",
  SUGGEST: "건의사항",
  PARTY: "공략대모집",
  GUILD: "길드모집",
  FRIEND: "깐부모집",
};

export default function Community() {
  const [selected, setSelected] = useState("자유게시판");
  const [posts, setPosts] = useState([]);
  const [selectedPost, setSelectedPost] = useState(null);

  const [profileCache, setProfileCache] = useState({}); // 프로필 캐싱

  // 글쓰기
  const [openWrite, setOpenWrite] = useState(false);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [images, setImages] = useState([]);
  const [imagePreviews, setImagePreviews] = useState([]);


  

  // 로스트아크 프로필 이미지 가져오기
  const loadProfileImage = async (nickname) => {
    if (profileCache[nickname]) return; // 이미 있으면 패스
    
    try {
      const res = await fetch(`/api/user/profile?nickname=${nickname}`);
      const data = await res.json();

      const imageUrl = data?.characterImage || null;

      setProfileCache(prev => ({
        ...prev,
        [nickname]: imageUrl || defaultProfile,
      }));
    } catch {
      setProfileCache(prev => ({
        ...prev,
        [nickname]: defaultProfile,
      }));
    }
  };

  // 게시글 목록 로드
  useEffect(() => {
    loadPosts();
  }, [selected]);

  const loadPosts = async () => {
    try {
      const backendCategory = categoryMap[selected];
      const res = await fetch(`/api/board/list?category=${backendCategory}`);
      const data = await res.json();
      setPosts(data);

      // 목록 로드시 프로필 선로딩
      data.forEach((p) => loadProfileImage(p.nickname));

    } catch (err) {
      console.error(err);
    }
  };

  const koreanDate = (str) => {
    if (!str) return "";
    return str.replace("T", " ").substring(0, 16);
  };

  // 이미지 업로드
  const handleImageUpload = (e) => {
    const files = [...e.target.files];
    setImages(files);
    setImagePreviews(files.map((file) => URL.createObjectURL(file)));
  };

  // 글쓰기 제출
  const handleSubmit = async () => {
    if (!title.trim()) {
      alert("제목을 입력하세요.");
      return;
    }

    const form = new FormData();
    const dto = {
      title,
      content,
      categoryCode: categoryMap[selected],
      hideNickname: false,
    };

    form.append("data", new Blob([JSON.stringify(dto)], { type: "application/json" }));
    images.forEach((file) => form.append("files", file));

    try {
      const res = await fetch("/api/board/create", {
        method: "POST",
        body: form,
      });

      if (!res.ok) throw new Error("글쓰기 실패");

      setOpenWrite(false);
      setTitle("");
      setContent("");
      setImages([]);
      setImagePreviews([]);

      loadPosts();
    } catch (err) {
      console.error(err);
      alert("글쓰기 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="page-container">
      <h2>💬 커뮤니티</h2>

      {/* 카테고리 */}
      <div className="category-bar">
        {categories.map((cat) => (
          <button
            key={cat}
            className={selected === cat ? "active" : ""}
            onClick={() => setSelected(cat)}
          >
            {cat}
          </button>
        ))}
      </div>

      {/* 글쓰기 버튼 */}
      <div className="board-controls">
        <button onClick={() => setOpenWrite(true)}>✍️ 글쓰기</button>
      </div>

      {/* 글쓰기 모달 */}
      {openWrite && (
        <div className="modal-overlay" onClick={() => setOpenWrite(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>✍️ 글쓰기 [{selected}]</h3>

            <input
              className="title-input"
              placeholder="제목"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />

            <textarea
              className="content-input"
              placeholder="내용을 입력하세요"
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />

            <label className="image-upload-btn">
              📷 이미지 추가
              <input type="file" accept="image/*" multiple onChange={handleImageUpload} />
            </label>

            <div className="image-preview-wrapper">
              {imagePreviews.map((src, i) => (
                <img key={i} src={src} className="image-preview" alt="preview" />
              ))}
            </div>

            <div className="modal-buttons">
              <button onClick={handleSubmit}>등록</button>
              <button onClick={() => setOpenWrite(false)}>취소</button>
            </div>
          </div>
        </div>
      )}

      {/* 게시글 리스트 */}
      <div className="post-list">
        {posts.length === 0 ? (
          <p className="empty">게시글이 없습니다.</p>
        ) : (
          posts.map((post) => (
            <div
              key={post.id}
              className="post-card"
              onClick={() => setSelectedPost(post)}
            >
              <div className="post-top">
                <img
                  src={profileCache[post.nickname] || defaultProfile}
                  className="post-profile"
                  alt="profile"
                />

                <div className="post-top-info">
                  <div className="post-nickname-row">
                    <span className="post-nickname">{post.nickname}</span>
                    <span className="post-dot">·</span>
                    <span className="post-category">
                      {categoryToKorean[post.category]}
                    </span>
                  </div>
                  <span className="post-date">{koreanDate(post.createdAt)}</span>
                </div>
              </div>

              <div className="post-content">{post.content}</div>

              {post.imageUrls && post.imageUrls.length > 0 && (
                <div className="post-img-box">
                  {post.imageUrls.map((url, i) => (
                    <img key={i} src={url} className="post-img" alt="" />
                  ))}
                </div>
              )}

            <div className="post-meta">
              <span className="meta-item">
                <img src={commentIcon} alt="comment" className="meta-icon" />
                {post.commentCount}
              </span>

              <span className="meta-item">
                <img src={viewIcon} alt="views" className="meta-icon" />
                {post.viewCount}
              </span>
            </div>


            </div>
          ))
        )}
      </div>

      {selectedPost && (
        <BoardDetailModal
          post={selectedPost}
          onClose={() => setSelectedPost(null)}
          onUpdate={(updated) => {
            // updated: /detail 에서 받은 최신 BoardDetailDto
            setPosts((prev) =>
              prev.map((p) =>
                p.id === updated.id
                  ? {
                      ...p,
                      viewCount: updated.viewCount,       // 조회수 동기화
                      commentCount: updated.commentCount, // 댓글수 동기화 (원하면)
                      likeCount: updated.likeCount,       // 좋아요 동기화 (원하면)
                    }
                  : p
              )
            );
          }}
        />
      )}

    </div>
  );
}
