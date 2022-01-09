package cn.tx.sboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@MapperScan("cn.tx.sboot.mapper")
public class MybatisSpringConfig1 {

	@Bean
	public DruidDataSource dataSource(){
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/txjava?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		return dataSource;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		return factoryBean.getObject();
	}


	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(){
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage("cn.tx.sboot.mapper");
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		//mapperScannerConfigurer.setAnnotationClass(Mapper.class);
		return mapperScannerConfigurer;
	}
	/*@Bean
	public MapperFactoryBean mapperFactoryBean() throws Exception {
		MapperFactoryBean mapperFactoryBean = new MapperFactoryBean(PersonMapper.class);
		SqlSessionFactory sqlSessionFactory = sqlSessionFactory();
		sqlSessionFactory.getConfiguration().addMapper(PersonMapper.class);
		mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
		return mapperFactoryBean;
	}*/

	/*@Bean
	public PersonMapper personMapper() throws Exception {
		return (PersonMapper) mapperFactoryBean().getObject();
	}*/
}
