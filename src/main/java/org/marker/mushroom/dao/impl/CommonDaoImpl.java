package org.marker.mushroom.dao.impl;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.ICommonDao;
import org.marker.mushroom.dao.annotation.Entity;
import org.springframework.stereotype.Repository;



/**
 * 通用数据库操作对象
 * 
 * @author marker
 * @date 2013-11-15 下午5:29:28
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@Repository(DAO.COMMON)
public class CommonDaoImpl extends DaoEngine implements ICommonDao
{




	// 批量删除
	@Override
	public boolean deleteByIds(final Class<?> clzz, final String ids)
	{
		final String prefix = dbConfig.getPrefix();// 表前缀
		final String tableName = clzz.getAnnotation(Entity.class).value();
		final String primaryKey = clzz.getAnnotation(Entity.class).key();

		final StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(prefix).append(tableName).append(" where ").append(primaryKey).append(" in(").append(ids)
				.append(")");
		logger(sql.toString());
		return jdbcTemplate.update(sql.toString()) > 0;
	}


	@Override
	public boolean top(final Class<?> clzz, final String rid, final String level)
	{
		final String prefix = dbConfig.getPrefix(); //获取数据库前缀
		final String tableName = clzz.getAnnotation(Entity.class).value();
		final String primaryKey = clzz.getAnnotation(Entity.class).key();

		final StringBuffer sql = new StringBuffer();

		sql.append("update ");
		sql.append(prefix).append(tableName);
		sql.append(" set top = ");
		sql.append(level);
		sql.append(" where 1=1 and ");
		sql.append(primaryKey);
		sql.append(" in (");
		sql.append(rid);
		sql.append(")");

		final int status = jdbcTemplate.update(sql.toString());

		return status > 0;
	}
}