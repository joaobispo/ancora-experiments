/*
 *  Copyright 2010 Ancora Research Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.ancora.Releaser;

import org.ancora.SharedLibrary.Interfaces.EnumKey;
import org.ancora.SharedLibrary.Preferences.EnumPreferences;



/**
 * Implementation of EnumKey, with the keys to the Preferences of this program
 * and access to a PreferencesEnum.
 *
 * @author Joao Bispo
 */
public enum ReleaserPreferences implements EnumKey {

   ReleaseName("release-1.0"),
   DistFolder("./"),
   RunFolder("./"),
   RunFolderEnabled("true"),
   BuildJavadocZipEnabled("true"),
   OutputFolder("./");

   /**
    * Enum Constructor
    * 
    * @param defaultValue
    */
   private ReleaserPreferences(String defaultValue) {
      this.defaultValue = defaultValue;
   }


   @Override
   public String getKey() {
      return this.name();
   }

   @Override
   public String getDefaultValue() {
      return defaultValue;
   }

   /**
    * @return PreferencesEnum object associated with this program.
    */
   public static EnumPreferences getPreferences() {
      if(preferences == null) {
         preferences = initializePreferences();
      }

      return preferences;
   }


   /**
    * Initiallizes the Preferences object:
    *    - Asks for the Preferences associated with this package;
    *    - Looks for a Properties file and if found, loads and stores its
    *  definitions.
    *
    * @return a PreferencesEnum initialized for the ClientModule package.
    */
   private static EnumPreferences initializePreferences() {
      // Build Preferences
      EnumPreferences newPreferences = new EnumPreferences(ReleaserPreferences.class, true);

      return newPreferences;
   }

   /**
    * INSTANCE VARIABLES
    */
   private static EnumPreferences preferences = null;
   private final String defaultValue;

}
