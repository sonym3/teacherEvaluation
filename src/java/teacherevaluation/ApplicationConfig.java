/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teacherevaluation;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author HP
 */
@javax.ws.rs.ApplicationPath("teachereval")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(teacherevaluation.Crendentials.class);
        resources.add(teacherevaluation.Persons.class);
        resources.add(teacherevaluation.Rating.class);
        resources.add(teacherevaluation.Student.class);
        resources.add(teacherevaluation.StudentSubject.class);
        resources.add(teacherevaluation.Subject.class);
        resources.add(teacherevaluation.Teacher.class);


    }
    
}
