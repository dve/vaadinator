package de.akquinet.engineering.vaadinator.dao;

import de.akquinet.engineering.vaadinator.annotations.DisplayBean;
import de.akquinet.engineering.vaadinator.annotations.DisplayBeanSetting;
import de.akquinet.engineering.vaadinator.annotations.DisplayEnum;
import de.akquinet.engineering.vaadinator.annotations.DisplayProperty;
import de.akquinet.engineering.vaadinator.annotations.DisplayPropertySetting;
import de.akquinet.engineering.vaadinator.annotations.ServiceBean;
import de.akquinet.engineering.vaadinator.annotations.WrappedBean;
import net.java.dev.hickory.prism.GeneratePrism;
import net.java.dev.hickory.prism.GeneratePrisms;

@GeneratePrisms({ @GeneratePrism(value = DisplayBean.class, publicAccess = true),
		@GeneratePrism(value = DisplayBeanSetting.class, publicAccess = true),
		@GeneratePrism(value = DisplayEnum.class, publicAccess = true),
		@GeneratePrism(value = DisplayProperty.class, publicAccess = true),
		@GeneratePrism(value = DisplayPropertySetting.class, publicAccess = true),
		@GeneratePrism(value = ServiceBean.class, publicAccess = true),
		@GeneratePrism(value = WrappedBean.class, publicAccess = true) })
public class PrisimGenerator {

}
