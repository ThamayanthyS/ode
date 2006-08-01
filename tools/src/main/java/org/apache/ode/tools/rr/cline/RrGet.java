/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.     
 */
package org.apache.ode.tools.rr.cline;

import org.apache.ode.utils.StreamUtils;
import org.apache.ode.utils.cli.*;
import org.apache.ode.utils.rr.ResourceRepository;
import org.apache.ode.utils.rr.ResourceRepositoryException;
import org.apache.ode.utils.rr.URLResourceRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Command-line tool for getting (displaying) resources in a resource
 * repository.
 */
public class RrGet extends BaseCommandlineTool {
  
  private static final Argument ARG_RRDIR = new Argument("rr-dir",
      "resource repository directory"
      ,false);
  private static final Argument ARG_RESURI = new Argument("uri",
      "URI to retrieve",false);
  
  private static final Fragments CLINE = new Fragments(new CommandlineFragment[] {
    LOGGING, ARG_RRDIR, ARG_RESURI
  });
  
  private static final String SYNOPSIS = "retrieve a resource from a resource repository to standard out";
  
  public static void main(String[] args) {
  	setClazz(RrGet.class);
    if (args.length == 0 || HELP.matches(args)) {
      ConsoleFormatter.printSynopsis(getProgramName(),SYNOPSIS, new Fragments[] {
        CLINE, HELP
      });
      System.exit(0);
    } else if (!CLINE.matches(args)) {
      consoleErr("INVALID COMMANDLINE: Try \"" + getProgramName() + " -h\" for help.");
      System.exit(-1);
    }
    registerTempFileManager();
    initLogging();
    boolean quiet = QUIET_F.isSet();
    
    URI uri;
		try {
			uri = new URI(ARG_RESURI.getValue());
		} catch (URISyntaxException ex) {
			consoleErr("Malformed URI " + ARG_RESURI.getValue());
			System.exit(-2);
			throw new IllegalStateException();
		}
    
    ResourceRepository rr;
    try {
    	rr = new URLResourceRepository(new File(ARG_RRDIR.getValue()).toURI());
    } catch (ResourceRepositoryException zre) {
      consoleErr(zre.getMessage());
      System.exit(-2);
      throw new IllegalStateException();
    }
    
    if (rr.containsResource(uri)) {
      try {
        InputStream is = rr.resourceAsStream(uri);
        StreamUtils.copy(System.out,is);
        rr.close();
      } catch (IOException ioe) {
        consoleErr("IO Error reading resource: " + ioe.getMessage());
        System.exit(-4);
      }
    } else {
      if(!quiet) {
        consoleErr(ARG_RESURI.getValue() + " is not present in the repository.");
        System.exit(-3);
      }
    }
    System.exit(0);
  }
}
