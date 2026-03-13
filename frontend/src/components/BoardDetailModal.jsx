import { useEffect, useState } from "react";
import "../styles/board.css";
import defaultProfile from "../assets/default-profile.png";

export default function BoardDetailModal({ post, onClose, onUpdate }) {
  const [detail, setDetail] = useState(null);
  const [commentList, setCommentList] = useState([]);

  const [comment, setComment] = useState("");
  const [replyInputs, setReplyInputs] = useState({});
  const [replyTarget, setReplyTarget] = useState(null);

  const [profileUrl, setProfileUrl] = useState(defaultProfile);

  useEffect(() => {
    loadDetail();
  }, [post.id]);

  const loadDetail = async () => {
    const res = await fetch(`/api/board/detail?id=${post.id}`);
    const data = await res.json();

    setDetail(data);

    loadProfileImage(data.nickname);
    loadComments(data.id);

    if (onUpdate) {
      onUpdate(data);
    }
  };

  const loadComments = async () => {
    const res = await fetch(`/api/comment/list?boardId=${post.id}`);
    const data = await res.json();
    setCommentList(data);
  };

  const loadProfileImage = async (nickname) => {
    try {
      const res = await fetch(`/api/user/profile?nickname=${nickname}`);
      const data = await res.json();
      const img = data.characterImage ? data.characterImage : defaultProfile;
      setProfileUrl(img);
    } catch {
      setProfileUrl(defaultProfile);
    }
  };

  const koreanDate = (str) => {
    if (!str) return "";
    return str.replace("T", " ").substring(0, 16);
  };

  const submitComment = async () => {
    const content = replyTarget ? replyInputs[replyTarget.id] || "" : comment;

    if (!content.trim()) {
      alert("내용을 입력해주세요.");
      return;
    }

    const dto = {
      boardId: post.id,
      content,
      parentId: replyTarget ? replyTarget.id : null,
    };

    const res = await fetch("/api/comment", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dto),
    });

    if (!res.ok) {
      alert("댓글 등록 실패");
      return;
    }

    // 입력창/상태 초기화
    setComment("");
    setReplyInputs((prev) => ({ ...prev, [replyTarget?.id]: "" }));
    setReplyTarget(null);

    // ⭐ 댓글 작성 즉시 댓글 목록 새로고침
    loadComments();

    // ⭐ detail 조회수/댓글 수 갱신 (부모 리프레시)
    loadDetail();
  };

  if (!detail) return null;

  return (
    <div className="detail-overlay" onClick={onClose}>
      <div className="detail-modal" onClick={(e) => e.stopPropagation()}>
        <button className="detail-close" onClick={onClose}>
          ✖
        </button>

        <div className="detail-header">
          <img src={profileUrl} className="detail-profile" alt="profile" />
          <div>
            <div className="detail-nickname">{detail.nickname}</div>
            <div className="detail-date">{koreanDate(detail.createdAt)}</div>
          </div>
        </div>

        <div className="detail-content">{detail.content}</div>

        {detail.imageUrls?.length > 0 && (
          <div className="detail-images">
            {detail.imageUrls.map((src, idx) => (
              <img key={idx} src={src} className="detail-img" alt="" />
            ))}
          </div>
        )}

        <h3 className="comment-title">댓글 ({commentList.length})</h3>

        {/* ===== 기본 댓글 입력 ===== */}
        <div className="comment-box main-comment-box">
          <textarea
            placeholder="댓글을 입력해주세요."
            value={comment}
            onChange={(e) => setComment(e.target.value)}
          />
          <button className="comment-submit" onClick={submitComment}>
            게시하기
          </button>
        </div>

        {/* ===== 댓글 목록 ===== */}
        <div className="comment-list">
          {commentList.map((c) => (
            <div key={c.id} className="comment-card">
              <div className="comment-item">
                <img
                  src={c.profileImage || defaultProfile}
                  className="comment-profile"
                />

                <div className="comment-body">
                  <div className="comment-top">
                    {/* 왼쪽: 닉네임 */}
                    <div className="comment-info-row">
                        <span className="comment-nickname">{c.nickname}</span>
                    </div>
                    {/* 오른쪽: 날짜 */}
                    <span className="comment-date">{koreanDate(c.createdAt)}</span>
                  </div>

                  <div className="comment-content">{c.content}</div>

                  <div className="comment-actions">
                    <button
                      onClick={() => {
                        setReplyTarget(c);
                        setReplyInputs((prev) => ({
                          ...prev,
                          [c.id]: prev[c.id] || "",
                        }));
                      }}
                    >
                      답글
                    </button>
                  </div>

                  {replyTarget?.id === c.id && (
                    <div className="reply-input-box inside">
                      <textarea
                        placeholder={`@${c.nickname} 에게 대댓글 달기`}
                        value={replyInputs[c.id] || ""}
                        onChange={(e) =>
                          setReplyInputs((prev) => ({
                            ...prev,
                            [c.id]: e.target.value,
                          }))
                        }
                      />
                      <button className="reply-submit" onClick={submitComment}>
                        등록
                      </button>
                    </div>
                  )}

                  {/* ===== 대댓글 목록 ===== */}
                  {c.replies?.map((r) => (
                    <div key={r.id} className="reply-card">
                      <div className="reply-item">
                        <img
                          src={r.profileImage || defaultProfile}
                          className="reply-profile"
                        />

                        <div className="reply-body">
                          <div className="comment-top">
                             <div className="comment-info-row">
                                <span className="comment-nickname">{r.nickname}</span>
                             </div>
                            <span className="comment-date">
                              {koreanDate(r.createdAt)}
                            </span>
                          </div>

                          <div className="comment-content">
                            <span className="mention-tag">
                              @{r.parentNickname || c.nickname}
                            </span>{" "}
                            {r.content}
                          </div>

                          <div className="comment-actions">
                            <button
                              onClick={() => {
                                setReplyTarget(r);
                                setReplyInputs((prev) => ({
                                  ...prev,
                                  [r.id]: prev[r.id] || "",
                                }));
                              }}
                            >
                              답글
                            </button>
                          </div>

                          {replyTarget?.id === r.id && (
                            <div className="reply-input-box inside">
                              <textarea
                                placeholder={`@${r.nickname} 에게 답글 달기`}
                                value={replyInputs[r.id] || ""}
                                onChange={(e) =>
                                  setReplyInputs((prev) => ({
                                    ...prev,
                                    [r.id]: e.target.value,
                                  }))
                                }
                              />
                              <button
                                className="reply-submit"
                                onClick={submitComment}
                              >
                                등록
                              </button>
                            </div>
                          )}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
