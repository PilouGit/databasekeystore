/*
 *
 *
 * Copyright 2022 Pierre-Emmanuel Gros.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.github.pilougit.security;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class EntityManagerFactory
{
    public static EntityManager createEntityManagerHSQLDB()
    {
        javax.persistence.EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("HSQLDB");
        return emFactory.createEntityManager();
    }
}
