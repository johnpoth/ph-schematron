/**
 * Copyright (C) 2014 phloc systems (www.phloc.com)
 * Copyright (C) 2014 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.schematron.pure.errorhandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.error.EErrorLevel;
import com.helger.commons.io.IReadableResource;
import com.helger.commons.string.ToStringGenerator;
import com.helger.schematron.pure.model.IPSElement;

/**
 * Abstract implementation of {@link IPSErrorHandler}.
 * 
 * @author Philip Helger
 */
public abstract class AbstractPSErrorHandler implements IPSErrorHandler
{
  private final IPSErrorHandler m_aNestedErrorHandler;

  protected AbstractPSErrorHandler ()
  {
    this (null);
  }

  protected AbstractPSErrorHandler (@Nullable final IPSErrorHandler aNestedErrorHandler)
  {
    m_aNestedErrorHandler = aNestedErrorHandler;
  }

  @Nullable
  public IPSErrorHandler getNestedErrorHandler ()
  {
    return m_aNestedErrorHandler;
  }

  protected abstract void handle (@Nullable IReadableResource aRes,
                                  @Nonnull EErrorLevel eErrorLevel,
                                  @Nonnull IPSElement aSourceElement,
                                  @Nonnull String sMessage,
                                  @Nullable Throwable t);

  public final void warn (@Nullable final IReadableResource aRes,
                          @Nonnull final IPSElement aSourceElement,
                          @Nonnull final String sMessage)
  {
    handle (aRes, EErrorLevel.WARN, aSourceElement, sMessage, (Throwable) null);

    // Do we have a nested error handler?
    final IPSErrorHandler aNestedErrorHandler = getNestedErrorHandler ();
    if (aNestedErrorHandler != null)
      aNestedErrorHandler.warn (aRes, aSourceElement, sMessage);
  }

  public final void error (@Nullable final IReadableResource aRes,
                           @Nonnull final IPSElement aSourceElement,
                           @Nonnull final String sMessage,
                           @Nullable final Throwable t)
  {
    handle (aRes, EErrorLevel.ERROR, aSourceElement, sMessage, t);

    // Do we have a nested error handler?
    final IPSErrorHandler aNestedErrorHandler = getNestedErrorHandler ();
    if (aNestedErrorHandler != null)
      aNestedErrorHandler.error (aRes, aSourceElement, sMessage, t);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("nestedErrorHandler", m_aNestedErrorHandler).toString ();
  }
}
