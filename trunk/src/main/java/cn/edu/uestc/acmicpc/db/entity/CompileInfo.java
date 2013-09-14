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
 * Compile information which compiler returns.
 */
@Table(name = "compileInfo")
@Entity
@KeyField("compileInfoId")
public class CompileInfo implements Serializable {

  private static final long serialVersionUID = 1404496264299518630L;
  private Integer compileInfoId;

  private Integer version = 0;

  @Version
  @Column(name = "OPTLOCK")
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Column(name = "compileInfoId", nullable = false, insertable = true, updatable = true,
      length = 10, precision = 0, unique = true)
  @Id
  @GeneratedValue
  public Integer getCompileInfoId() {
    return compileInfoId;
  }

  public void setCompileInfoId(Integer compileInfoId) {
    this.compileInfoId = compileInfoId;
  }

  private String content;

  @Column(name = "content", nullable = false, insertable = true, updatable = true, length = 65535,
      precision = 0)
  @Basic
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    CompileInfo that = (CompileInfo) o;

    if (!compileInfoId.equals(that.compileInfoId))
      return false;
    if (content != null ? !content.equals(that.content) : that.content != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = compileInfoId;
    result = 31 * result + (content != null ? content.hashCode() : 0);
    return result;
  }

  private Collection<Status> statusesByCompileInfoId;

  @OneToMany(mappedBy = "compileInfoByCompileInfoId", cascade = CascadeType.ALL)
  public Collection<Status> getStatusesByCompileInfoId() {
    return statusesByCompileInfoId;
  }

  public void setStatusesByCompileInfoId(Collection<Status> statusesByCompileInfoId) {
    this.statusesByCompileInfoId = statusesByCompileInfoId;
  }
}
