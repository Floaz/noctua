/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noctuasource.act.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Philipp Thomas
 */
@Target( { ElementType.METHOD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface RunLater {

}
