package cn.edu.uestc.acmicpc.db.condition.impl;

import cn.edu.uestc.acmicpc.db.condition.base.BaseCondition;
import cn.edu.uestc.acmicpc.db.condition.base.Condition;
import cn.edu.uestc.acmicpc.util.enums.ProblemType;
import cn.edu.uestc.acmicpc.util.exception.AppException;
import cn.edu.uestc.acmicpc.util.helper.EnumTypeUtil;

import java.util.Objects;

/**
 * Problem database condition entity.
 */
@Deprecated
public class ProblemCondition extends BaseCondition {

  public ProblemCondition() {
    super("problemId");
  }

  /**
   * Start problem id.
   */
  @Exp(mapField = "problemId", type = Condition.ConditionType.GREATER_OR_EQUALS)
  public Integer startId;

  /**
   * End problem id.
   */
  @Exp(mapField = "problemId", type = Condition.ConditionType.LESS_OR_EQUALS)
  public Integer endId;

  /**
   * Problem title (partly matches)
   */
  @Exp(type = Condition.ConditionType.LIKE)
  public String title;

  /**
   * Problem source (partly matches)
   */
  @Exp(type = Condition.ConditionType.LIKE)
  public String source;

  /**
   * Search keyword
   */
  public String keyword;

  /**
   * SPJ state
   */
  @Exp(type = Condition.ConditionType.EQUALS)
  public Boolean isSpj;

  /**
   * Is problem visible?
   */
  @Exp(type = Condition.ConditionType.EQUALS)
  public Boolean isVisible;

  /**
   * Minimal problem difficulty
   */
  @Exp(mapField = "difficulty", type = Condition.ConditionType.GREATER_OR_EQUALS)
  public Integer startDifficulty;

  /**
   * Maximal problem difficulty
   */
  @Exp(mapField = "difficulty", type = Condition.ConditionType.LESS_OR_EQUALS)
  public Integer endDifficulty;

  /**
   * Is problem has an empty title?
   */
  public Boolean isTitleEmpty;

  /**
  * The problem type to be searched.
  */
  @Exp(type = Condition.ConditionType.EQUALS)
  public ProblemType type;

  @Override
  public Condition getCondition() throws AppException {
    Condition condition = EnumTypeUtil.toOrdinalCondition(super.getCondition());
    if (isTitleEmpty != null) {
      if (isTitleEmpty) {
        condition.addEntry("title", Condition.ConditionType.STRING_EQUALS, "");
      } else {
        condition.addEntry("title", Condition.ConditionType.STRING_EQUALS, "_%");
      }
    }
    if (keyword != null) {
      Condition keywordCondition = new Condition(Condition.JoinedType.OR);
      keywordCondition.addEntry("title", Condition.ConditionType.LIKE, keyword);
      keywordCondition.addEntry("description", Condition.ConditionType.LIKE, keyword);
      keywordCondition.addEntry("source", Condition.ConditionType.LIKE, keyword);
      condition.addEntry(keywordCondition);
    }
    return condition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProblemCondition)) {
      return false;
    }

    ProblemCondition that = (ProblemCondition) o;
    return Objects.equals(this.startId, that.startId)
        && Objects.equals(this.endId, that.endId)
        && Objects.equals(this.title, that.title)
        && Objects.equals(this.source, that.source)
        && Objects.equals(this.keyword, that.keyword)
        && Objects.equals(this.isSpj, that.isSpj)
        && Objects.equals(this.isVisible, that.isVisible)
        && Objects.equals(this.startDifficulty, that.startDifficulty)
        && Objects.equals(this.endDifficulty, that.endDifficulty)
        && Objects.equals(this.isTitleEmpty, that.isTitleEmpty);
  }

  @Override
  public int hashCode() {
    int result = 0;
    result = 31 * result + (startId != null ? startId.hashCode() : 0);
    result = 31 * result + (endId != null ? endId.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (source != null ? source.hashCode() : 0);
    result = 31 * result + (keyword != null ? keyword.hashCode() : 0);
    result = 31 * result + (isSpj != null ? isSpj.hashCode() : 0);
    result = 31 * result + (isVisible != null ? isVisible.hashCode() : 0);
    result = 31 * result + (startDifficulty != null ? startDifficulty.hashCode() : 0);
    result = 31 * result + (endDifficulty != null ? endDifficulty.hashCode() : 0);
    result = 31 * result + (isTitleEmpty != null ? isTitleEmpty.hashCode() : 0);
    return result;
  }
}
