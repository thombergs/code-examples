package io.reflectoring.beanlifecycle.ipdatabase;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Service;

@Service
public class IpToLocationService implements BeanFactoryAware {

    DefaultListableBeanFactory listableBeanFactory;
    IpDatabaseRepository ipDatabaseRepository;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        updateIpDatabase();
    }

    public void updateIpDatabase(){
        String updateUrl = "https://download.acme.com/latest/ip-address-database.mdb";

        AbstractBeanDefinition definition = BeanDefinitionBuilder
                .genericBeanDefinition(IpDatabaseRepository.class)
                .addConstructorArgValue(updateUrl)
                .getBeanDefinition();

        listableBeanFactory.registerBeanDefinition("ipDatabaseRepository", definition);
        this.ipDatabaseRepository = listableBeanFactory.getBean(IpDatabaseRepository.class);
    }

    public String getCountry(String ipAddress){
        return ipDatabaseRepository.lookup(ipAddress);
    }
}
