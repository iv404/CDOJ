/*
 *
 *  * cdoj, UESTC ACMICPC Online Judge
 *  * Copyright (c) 2013 fish <@link lyhypacm@gmail.com>,
 *  * 	mzry1992 <@link muziriyun@gmail.com>
 *  *
 *  * This program is free software; you can redistribute it and/or
 *  * modify it under the terms of the GNU General Public License
 *  * as published by the Free Software Foundation; either version 2
 *  * of the License, or (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program; if not, write to the Free Software
 *  * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package cn.edu.uestc.acmicpc.db.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import cn.edu.uestc.acmicpc.util.annotation.KeyField;

/**
 * Problem tag information.
 */
@Table(name = "tag")
@Entity
@KeyField("tagId")
public class Tag implements Serializable {

  private static final long serialVersionUID = 8221283073294354906L;
  private Integer tagId;

  private Integer version = 0;

  @Version
  @Column(name = "OPTLOCK")
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Column(name = "tagId", nullable = false, insertable = true, updatable = true, length = 10,
      precision = 0, unique = true)
  @Id
  @GeneratedValue
  public Integer getTagId() {
    return tagId;
  }

  public void setTagId(Integer tagId) {
    this.tagId = tagId;
  }

  private String name;

  @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 50,
      precision = 0, unique = true)
  @Basic
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Tag tag = (Tag) o;

    if (!tagId.equals(tag.tagId))
      return false;
    if (name != null ? !name.equals(tag.name) : tag.name != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = tagId;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  private Collection<ProblemTag> problemTagsByTagId;

  @OneToMany(mappedBy = "tagByTagId", cascade = CascadeType.ALL)
  public Collection<ProblemTag> getProblemTagsByTagId() {
    return problemTagsByTagId;
  }

  public void setProblemTagsByTagId(Collection<ProblemTag> problemTagsByTagId) {
    this.problemTagsByTagId = problemTagsByTagId;
  }
}
